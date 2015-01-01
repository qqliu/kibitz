package kibitz;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import kibitz.RecommenderService.Iface;

import org.apache.mahout.cf.taste.impl.recommender.AbstractRecommender;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import static java.util.concurrent.TimeUnit.*;

public class KibitzServer implements Iface {
	
	public static Map<String, IndividualRecommender> SESSIONS = new HashMap<String, IndividualRecommender>();
	public static Map<String, AbstractRecommender> RECOMMENDERS = new HashMap<String, AbstractRecommender>();
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
	public List<Item> makeRecommendation(String key, long userId, long numRecs) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).makeRecommendation(userId, numRecs);
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
	public void initiateModel(String key, String table, String username, String password, String database) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
				SESSIONS.get(key).initiateModel(table, username, password, database);
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
    public boolean createNewRecommender(String username, String password, String database, String table,
    		String firstColumnName, String secondColumnName, String thirdColumnName,
    		String firstColumnType, String secondColumnType, String thirdColumnType) {
		try {
			this.dataModel = new DatahubDataModel(this.dataSource.getServerName(), database, 
				username,
				password,
				null);
			return this.dataModel.createNewRecommender(table, firstColumnName, secondColumnName, thirdColumnName,
					firstColumnType, secondColumnType, thirdColumnType);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public List<Item> getUserRatedItems(String key, long userId) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
				return SESSIONS.get(key).getUserRatedItems(userId);
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
		return false;
	}
	
	@Override
	public String createNewUser(String key, String username, String email, String password, boolean isKibitzUser) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).createNewUser(username, email, password, isKibitzUser);
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
	public long retrieveUserId(String key, String username, String password) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).retrieveUserId(username, password);
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
	
	@Override
	public List<Item> getItems(String key) {
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
	}
	
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
	public List<Item> getPageItems(String key, long page, long numPerPage) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).getPageItems(page, numPerPage);
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
	public List<Item> getSearchItems(String key, String query) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).getSearchItems(query);
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
	
	public class RecommenderRunnable implements Runnable {	
		public void run() {
			while (RUNNING) {
				Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
				
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(SESSIONS.size() != 0) {
					for (String key: SESSIONS.keySet()) {
						IndividualRecommender rec = SESSIONS.get(key);
						updateDataModel(rec);
						if(updateDataModel(rec))
							updateRecommender(RECOMMENDERS.get(rec.getTable() + rec.getUsername() + rec.getPassword() + rec.getDatabase()));
					}
				}
			}
		}
		
		private void updateRecommender(AbstractRecommender recommender) {
			//recommender.clear();
			recommender.refresh(null);
		}
		
		private boolean updateDataModel(IndividualRecommender rec) {
			rec.updateDataModel();
			return rec.getRefreshed();
		}
	}
	
	public class TerminateModels implements Runnable {	
		public void run() {
			while (RUNNING) {
				Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
				
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
}
