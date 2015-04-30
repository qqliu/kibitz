package kibitz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import kibitz.RecommenderService.Iface;

import org.apache.mahout.cf.taste.impl.recommender.AbstractRecommender;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransportException;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import sun.org.mozilla.javascript.internal.NativeArray;
import sun.org.mozilla.javascript.internal.NativeObject;
import datahub.Connection;
import datahub.ConnectionParams;
import datahub.DBException;
import datahub.DataHub;
import datahub.ResultSet;
import datahub.Tuple;
import static java.util.concurrent.TimeUnit.*;

public class KibitzServer implements Iface {
	
	public static Map<String, IndividualRecommender> SESSIONS = new HashMap<String, IndividualRecommender>();
	public static Map<String, AbstractRecommender> RECOMMENDERS = new HashMap<String, AbstractRecommender>();
	public static String HOMEPAGE_URL = "localhost/kibitz-demo/home/";
	public static boolean RUNNING = true;
	
	private MysqlDataSource dataSource;
	private DatahubDataModel dataModel = null;
	private Thread loop = null;
	private Thread terminateModelRecs = null;
	
	public KibitzServer(MysqlDataSource dataSource) {
		this.dataSource = dataSource;
		
		// Start thread to continuously train recommenders and terminate recommenders
		if (this.loop == null) {
			Thread training = new Thread(new RecommenderRunnable());
			Thread terminateModel = new Thread(new TerminateModels());
			training.setName("Training Thread");
			terminateModel.setName("Terminate Model Thread");
			this.loop = training;
			this.loop.start();
			this.terminateModelRecs = terminateModel;
			this.terminateModelRecs.start();
		}
	}
	
	@Override
	public void createNewIndividualServer(String key) {
		if (key != null) {
			if (SESSIONS.get(key) == null) {
				SESSIONS.put(key, IndividualRecommender.createNewIndividualServer(this.dataSource));
			}
		}
	}
	
	@Override
	public List<Item> makeRecommendation(String key, long userId, long numRecs, boolean isBoolean, List<String> displayColumns) {
		if(!this.loop.isAlive()) {
			Thread training = new Thread(new RecommenderRunnable());
			training.setName("Training Thread");
			this.loop = training;
			this.loop.start();
		}
		
		if(!this.terminateModelRecs.isAlive()) {
			Thread terminateModel = new Thread(new TerminateModels());
			terminateModel.setName("Terminate Model Thread");			
			this.terminateModelRecs = terminateModel;
			this.terminateModelRecs.start();
		}
		
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).makeRecommendation(userId, numRecs, isBoolean, displayColumns);
			}
		}
		
		return null;
	}
	
	@Override
	public List<Item> makeItemBasedRecommendations(String key, long userId, long numRecs, List<String> displayColumns) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).makeItemBasedRecommendations(userId, numRecs, displayColumns);
			}
		}
		
		if(!this.loop.isAlive()) {
			Thread training = new Thread(new RecommenderRunnable());
			training.setName("Training Thread");
			this.loop = training;
			this.loop.start();
		}
		
		if(!this.terminateModelRecs.isAlive()) {
			Thread terminateModel = new Thread(new TerminateModels());
			terminateModel.setName("Terminate Model Thread");			
			this.terminateModelRecs = terminateModel;
			this.terminateModelRecs.start();
		}
		return null;
	}
	
	@Override
	public List<Item> makeOverallRatingBasedOrRandomRecommendation(String key, String ratingColumnName, 
			long numRecs, List<String> displayColumns) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				if (ratingColumnName != null) {
					return SESSIONS.get(key).makeOverallRatingBasedRecommendation(ratingColumnName, numRecs, displayColumns);
				} else {
					return SESSIONS.get(key).makeRandomRecommendation(numRecs, displayColumns);
				}
			}
		}
		return null;
	}
	
	@Override
	public List<Recommender> getRecommenders(String username) {
		try {
			THttpClient transport = new THttpClient("http://datahub.csail.mit.edu/service");
			TBinaryProtocol protocol = new  TBinaryProtocol(transport);
			DataHub.Client client = new DataHub.Client(protocol);
		
			ConnectionParams params = new ConnectionParams();
			params.setApp_id(DatahubDataModel.getKibitzAppName());
			params.setApp_token(DatahubDataModel.getKibitzAppId());
			params.setRepo_base(DatahubDataModel.getDefaultDatahubUsername());
			Connection connection = client.open_connection(params);
			
			List<Recommender> recommenders = new ArrayList<Recommender>();
		
		
			ResultSet res = client.execute_sql(connection, "SELECT database,username,ratings_table,overall_ratings,ratings_column FROM kibitz_users.recommenders WHERE username = '" + username + "';", null);
			HashMap<String, Integer> colToIndex = DatahubDataModel.getFieldNames(res);			
			for (Tuple t : res.getTuples()) {
				List<ByteBuffer> cells = t.getCells();
				Recommender recommender = new Recommender();
				String database = new String(cells.get(colToIndex.get("database")).array());
				recommender.setUsername(new String(cells.get(colToIndex.get("username")).array()));
				recommender.setRepoName(database);
				recommender.setRecommenderName(new String(cells.get(colToIndex.get("ratings_table")).array()).split("\\.")[1]);
				if (Boolean.parseBoolean(new String(cells.get(colToIndex.get("overall_ratings")).array())))
					recommender.setRatingsColumn(new String(cells.get(colToIndex.get("ratings_column")).array()));
				
				ScriptEngineManager mgr = new ScriptEngineManager();
				ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
				
				File file = new File(DatahubDataModel.WEBSERVER_DIR + username + "/" + database + "/js/initiate.js");
				Reader reader = new FileReader(file);
				jsEngine.eval(reader);
				
				recommender.setClientKey(jsEngine.get("client_key").toString());
				recommender.setHomepage(jsEngine.get("homepage").toString());
				recommender.setTitle(jsEngine.get("title").toString());
				recommender.setDescription(jsEngine.get("description").toString());
				recommender.setVideo(jsEngine.get("video").toString());
				recommender.setImage(jsEngine.get("image").toString());
				recommender.setPrimaryKey(jsEngine.get("primary_key").toString());
				NativeArray nativeArray = (NativeArray) jsEngine.get("display_items");
				List<String> displayItems = new ArrayList<String>();
				
				for (int i=0; i < (int) nativeArray.getLength(); i++) {
					displayItems.add((String) nativeArray.get(i));
				}
				
				recommender.setDisplayItems(displayItems);
				System.out.println((NativeObject) jsEngine.get("item_types"));
				NativeObject nativeObject = (NativeObject) jsEngine.get("item_types");
				
				HashMap<String, String> itemMap = new HashMap<String, String>();
				
				for (Object key: nativeObject.getAllIds()) {
			        itemMap.put((String) key, (String) nativeObject.get(key));
			    }
				recommender.setItemTypes(itemMap);
				recommender.setNumRecs((int) Double.parseDouble(jsEngine.get("num_recs").toString()));
				recommender.setMaxRatingVal((int) Double.parseDouble(jsEngine.get("maxRatingVal").toString()));
				
				recommenders.add(recommender);
			}
			
			return recommenders;
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public void initiateModel(String key, String table, String username, String database) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
				SESSIONS.get(key).initiateModel(key, table, username, database);
			}
		}
		
		if(!this.loop.isAlive()) {
			Thread training = new Thread(new RecommenderRunnable());
			training.setName("Training Thread");
			this.loop = training;
			this.loop.start();
		}
		
		if(!this.terminateModelRecs.isAlive()) {
			Thread terminateModel = new Thread(new TerminateModels());
			terminateModel.setName("Terminate Model Thread");			
			this.terminateModelRecs = terminateModel;
			this.terminateModelRecs.start();
		}
	}
	
	/*@Override
    public boolean createNewRecommender(String username, String primaryKey, String password, String database, String table,
    		String firstColumnName, String secondColumnName, String thirdColumnName,
    		String firstColumnType, String secondColumnType, String thirdColumnType,
    		List<String> displayColumns, String clientKey, String ratingsColumn, boolean random) {
		try {
			this.dataModel = new DatahubDataModel(this.dataSource.getServerName(), database, 
				username,
				password,
				table);
			return this.dataModel.createNewRecommender(table, primaryKey, firstColumnName, secondColumnName, thirdColumnName,
					firstColumnType, secondColumnType, thirdColumnType, displayColumns, clientKey, ratingsColumn, random);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}*/
	
	public boolean createNewRecommender(String username, String primaryKey, String database, String table,
    		String title, String description, String image, String ratings_column, String clientKey) {
		try {
			this.dataModel = new DatahubDataModel(this.dataSource.getServerName(), database, username, table);
			return this.dataModel.createNewRecommender(table, primaryKey, title, description, image, ratings_column, clientKey);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public void addKibitzUser(String email, String password) {
		try {
			DatahubDataModel model = new DatahubDataModel();
			model.addKibitzUser(email, password);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void saveFBProfilePic(String username, String fbUsername) {
		try {
			DatahubDataModel model = new DatahubDataModel();
			model.saveFBProfilePic(username, fbUsername);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void deleteRecommender(String clientKey) {
		try {
			DatahubDataModel model = new DatahubDataModel();
			model.deleteRecommender(clientKey);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateTemplate(String username, String primaryKey,
			String title, String description, String image, String video,
			Map<String, String> itemTypes, List<String> displayItems,
			long maxRatingVal, long numRecs, String recommenderName,
			String clientKey, String homepage, String creatorName,
			String repoName, String tableName, String ratingsColumn)
			throws TException {
		try {
			DatahubDataModel model = new DatahubDataModel();
			model.updateTemplate(username, primaryKey, title, description, image, video, itemTypes, 
					displayItems, (int) maxRatingVal, (int) numRecs, recommenderName, clientKey, homepage, 
					creatorName, repoName, tableName, ratingsColumn);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public List<Item> getUserRatedItems(String key, long userId, List<String> displayColumns) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
				return SESSIONS.get(key).getUserRatedItems(userId, displayColumns);
			}
		}
		
		if(!this.loop.isAlive()) {
			Thread training = new Thread(new RecommenderRunnable());
			training.setName("Training Thread");
			this.loop = training;
			this.loop.start();
		}
		
		if(!this.terminateModelRecs.isAlive()) {
			Thread terminateModel = new Thread(new TerminateModels());
			terminateModel.setName("Terminate Model Thread");			
			this.terminateModelRecs = terminateModel;
			this.terminateModelRecs.start();
		}
		
		return null;
    }
	
	@Override
	public boolean checkLogin(String key, String username, String password, boolean isKibitzUser) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).checkLogin(username, password, isKibitzUser);
			}
		}
		return false;
	}
	
	@Override
	public boolean checkUsername(String key, String username, boolean isKibitzUser) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).checkUsername(username, isKibitzUser);
			} 
		}
		else if (isKibitzUser) {
			DatahubDataModel model;
			try {
				model = new DatahubDataModel();
				return model.checkUsername(username, null, true);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
	@Override
	public String createNewUser(String key, String username, boolean isKibitzUser) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).createNewUser(username, isKibitzUser);
			}
		}
		
		if(!this.loop.isAlive()) {
			Thread training = new Thread(new RecommenderRunnable());
			training.setName("Training Thread");
			this.loop = training;
			this.loop.start();
		}
		
		if(!this.terminateModelRecs.isAlive()) {
			Thread terminateModel = new Thread(new TerminateModels());
			terminateModel.setName("Terminate Model Thread");			
			this.terminateModelRecs = terminateModel;
			this.terminateModelRecs.start();
		}
		
		return null;
	}
	
	@Override
	public long retrieveUserId(String key, String username) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).retrieveUserId(username);
			}
		}
		if(!this.loop.isAlive()) {
			Thread training = new Thread(new RecommenderRunnable());
			training.setName("Training Thread");
			this.loop = training;
			this.loop.start();
		}
		
		if(!this.terminateModelRecs.isAlive()) {
			Thread terminateModel = new Thread(new TerminateModels());
			terminateModel.setName("Terminate Model Thread");			
			this.terminateModelRecs = terminateModel;
			this.terminateModelRecs.start();
		}
		
		return 0;
	}
	
	/*public List<Item> getItems(String key) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).getItems();
			}
		}
		if(!this.loop.isAlive()) {
			Thread training = new Thread(new RecommenderRunnable());
			training.setName("Training Thread");
			this.loop = training;
			this.loop.start();
		}
		
		if(!this.terminateModelRecs.isAlive()) {
			Thread terminateModel = new Thread(new TerminateModels());
			terminateModel.setName("Terminate Model Thread");			
			this.terminateModelRecs = terminateModel;
			this.terminateModelRecs.start();
		}
		
		return null;
	}*/
	
	@Override
	public void recordRatings(String key, long userId, long itemId, long rating) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				SESSIONS.get(key).recordRatings(userId, itemId, rating);;
			}
		}
		if(!this.loop.isAlive()) {
			Thread training = new Thread(new RecommenderRunnable());
			training.setName("Training Thread");
			this.loop = training;
			this.loop.start();
		}
		
		if(!this.terminateModelRecs.isAlive()) {
			Thread terminateModel = new Thread(new TerminateModels());
			terminateModel.setName("Terminate Model Thread");			
			this.terminateModelRecs = terminateModel;
			this.terminateModelRecs.start();
		}
	}
	
	@Override
	public void deleteRatings(String key, long userId, long itemId) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				SESSIONS.get(key).deleteRatings(userId, itemId);
			}
		}
		if(!this.loop.isAlive()) {
			Thread training = new Thread(new RecommenderRunnable());
			training.setName("Training Thread");
			this.loop = training;
			this.loop.start();
		}
		
		if(!this.terminateModelRecs.isAlive()) {
			Thread terminateModel = new Thread(new TerminateModels());
			terminateModel.setName("Terminate Model Thread");			
			this.terminateModelRecs = terminateModel;
			this.terminateModelRecs.start();
		}
	}
	
	@Override
	public List<Item> getPageItems(String key, long page, long numPerPage, List<String> displayColumns) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).getPageItems(page, numPerPage, displayColumns);
			}
		}
		if(!this.loop.isAlive()) {
			Thread training = new Thread(new RecommenderRunnable());
			training.setName("Training Thread");
			this.loop = training;
			this.loop.start();
		}
		
		if(!this.terminateModelRecs.isAlive()) {
			Thread terminateModel = new Thread(new TerminateModels());
			terminateModel.setName("Terminate Model Thread");			
			this.terminateModelRecs = terminateModel;
			this.terminateModelRecs.start();
		}
		
		return null;
	}

	@Override
	public long getItemCount(String key) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).getItemCount();
			}
		}
		return 0;
	}
	
	@Override
	public void terminateSession(String key) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				SESSIONS.remove(key);
			}
		}
	}
	
	@Override
	public List<Item> getSearchItems(String key, String query, List<String> columnsToSearch, List<String> displayColumns) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).getSearchItems(query, columnsToSearch, displayColumns);
			}
		}
		if(!this.loop.isAlive()) {
			Thread training = new Thread(new RecommenderRunnable());
			training.setName("Training Thread");
			this.loop = training;
			this.loop.start();
		}
		
		if(!this.terminateModelRecs.isAlive()) {
			Thread terminateModel = new Thread(new TerminateModels());
			terminateModel.setName("Terminate Model Thread");			
			this.terminateModelRecs = terminateModel;
			this.terminateModelRecs.start();
		}
		
		return null;
	}
	
	/*@Override
	public List<Item> getItemsFromPrimaryKeys(String key, String primaryKey, List<String> itemKeys, List<String> displayColumns) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).getItemsFromPrimaryKeys(primaryKey, itemKeys, displayColumns);
			}
		}
		if(!this.loop.isAlive()) {
			Thread training = new Thread(new RecommenderRunnable());
			training.setName("Training Thread");
			this.loop = training;
			this.loop.start();
		}
		
		if(!this.terminateModelRecs.isAlive()) {
			Thread terminateModel = new Thread(new TerminateModels());
			terminateModel.setName("Terminate Model Thread");			
			this.terminateModelRecs = terminateModel;
			this.terminateModelRecs.start();
		}
		
		return null;
	}*/
	
	public class RecommenderRunnable implements Runnable {	
		public void run() {
			while (RUNNING) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(SESSIONS.size() != 0) {
					for (String key: SESSIONS.keySet()) {
						IndividualRecommender rec = SESSIONS.get(key);
						if(updateDataModel(rec, key))
							updateRecommender(RECOMMENDERS.get(key));
					}
				}
			}
		}
		
		private void updateRecommender(AbstractRecommender recommender) {
			//recommender.clear();
			recommender.refresh(null);
		}
		
		private boolean updateDataModel(IndividualRecommender rec, String key) {
			rec.updateDataModel(key);
			return rec.getRefreshed();
		}
	}
	
	public class TerminateModels implements Runnable {	
		public void run() {
			while (RUNNING) {				
				try {
					Thread.sleep(400000000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(SESSIONS.size() != 0) {
					for (String key: SESSIONS.keySet()) {
						IndividualRecommender rec = SESSIONS.get(key);
						String stamp = Pattern.quote(rec.getTimestamp()).split("\\.")[0].substring(2);
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						Date lastUpdateTime;
						try {
							lastUpdateTime = formatter.parse(stamp);
							Date today = new Date();
							if ((today.getTime() - lastUpdateTime.getTime()) >= MILLISECONDS.convert(20, DAYS)) {
								SESSIONS.remove(key);
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	@Override
	public boolean checkCorrectDatahubLogin(String username, String repository, String table, String primary_key, String title,
			String description, String image) {
		try {
			THttpClient transport = new THttpClient("http://datahub.csail.mit.edu/service");
			TBinaryProtocol protocol = new  TBinaryProtocol(transport);
			DataHub.Client client = new DataHub.Client(protocol);
		
			ConnectionParams params = new ConnectionParams();
			params.setApp_id(DatahubDataModel.getKibitzAppName());
			params.setApp_token(DatahubDataModel.getKibitzAppId());
			params.setRepo_base(username);
			Connection connection = client.open_connection(params);
			
			client.execute_sql(connection, "Select * from " + repository + "." + table + " limit 0;", null);
			client.execute_sql(connection, "select " + primary_key + " from " + repository + "." + table + " limit 0;", null);
			
			if (!title.equals("no_kibitz_title")) {
				client.execute_sql(connection, "select " + title + " from " + repository + "." + table + " limit 0;", null);
			}
			
			if (!description.equals("no_kibitz_description")) {
				client.execute_sql(connection, "select " + description + " from " + repository + "." + table + " limit 0;", null);
			}
			
			if (!image.equals("no_kibitz_image")) {
				client.execute_sql(connection, "select " + image + " from " + repository + "." + table + " limit 0;", null);
			}
			return true;
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			return false;
		} catch (DBException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			return false;
		} catch (TException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	@Override
	public boolean checkRatingsColumn(String username, String repository, String table,
			String ratings_column) throws TException {
		try {
			THttpClient transport = new THttpClient("http://datahub.csail.mit.edu/service");
			TBinaryProtocol protocol = new  TBinaryProtocol(transport);
			DataHub.Client client = new DataHub.Client(protocol);
		
			ConnectionParams params = new ConnectionParams();
			params.setApp_id(DatahubDataModel.getKibitzAppName());
			params.setApp_token(DatahubDataModel.getKibitzAppId());
			params.setRepo_base(username);
			Connection connection = client.open_connection(params);
			
			if (!ratings_column.equals(""))
				client.execute_sql(connection, "select distinct pg_typeof(" + ratings_column + ") from " + repository + "." + table + " limit 1;", null);

			return true;
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			return false;
		} catch (DBException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			return false;
		} catch (TException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	@Override
	public String getProfilePicture(String username) {
		// TODO Auto-generated method stub
		try {
			DatahubDataModel model = new DatahubDataModel();
			return model.getProfilePicture(username);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public void configurePrefilledUserRatings(String username, String repoName,
			String primaryKey, String itemTable, String tableName,
			String userIdCol, String itemIdCol, String userRatingCol) {
		try {
			DatahubDataModel model = new DatahubDataModel();
			model.configurePrefilledUserRatings( username,  repoName,
					 primaryKey,  itemTable,  tableName,
					 userIdCol,  itemIdCol,  userRatingCol);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
