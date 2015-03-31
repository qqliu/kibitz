package kibitz;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.zeroturnaround.zip.ZipUtil;

import updates.UpdateLocalFiles;
import datahub.*;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.Lin;
import edu.cmu.lti.ws4j.impl.Path;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

public class DatahubDataModel implements DataModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** Default Datahub host. */
	private static final String DEFAULT_DATAHUB_HOST = "http://datahub.csail.mit.edu/service";
	
	/** Default Datahub user. */
	private static final String DEFAULT_DATAHUB_USERNAME = "quanquan";
	
	/**Default Datahub password. */
	private static final String DEFAULT_DATAHUB_PASSWORD = "hof9924ne@!";

	/** Default Datahub Database */
	private static final String DEFAULT_DATAHUB_DATABASE = "quanquan.kibitz_users";
	
	/** Default Datahub Table Name*/
	private static final String DEFAULT_DATAHUB_TABLENAME = "users";
	
	/** Default Webserver Location*/
	private static final String WEBSERVER_DIR = "/Applications/XAMPP/htdocs/kibitz-demo/home/";
	
	
	private String datahubHost = getDefaultDatahubHost();
	private String datahubUsername = getDefaultDatahubUsername();
	private String datahubPassword = getDefaultDatahubPassword();
	private String datahubDatabase = DEFAULT_DATAHUB_DATABASE;
	private String datahubTableName = getDefaultDatahubTablename();
	private String datahubOriginalTable = DEFAULT_DATAHUB_TABLENAME;
	private FileDataModel delegate = null;
	
	private static ILexicalDatabase db = new NictWordNet();
    private static RelatednessCalculator[] rcs = { new WuPalmer(db), new Lin(db), new Path(db) };

	
	private String lastTimestamp;
	private boolean refreshed;
	
	private TTransport transport;
	private TProtocol protocol;
	private DataHub.Client client;
	private ConnectionParams con_params;
	private Connection conn;

	public HashMap<Integer, CreateItemSimilarityRunnable> activeThreads;

	private String ratingsColumn;

	private boolean random;

	private boolean itemRatings;

	private int item_count;

	/**
	* Creates a new DatahubDataModel
	*/
	public DatahubDataModel() throws UnknownHostException {
		buildModel();
	}
	  
	/**
	 * Creates a new DatahubDataModel with customized Database configuration
	 * (with authentication)
	 *
	 * @param host        Datahub host.
	 * @param user     	  Datahub username (authentication)
	 * @param password    Datahub password (authentication)
	 * @param database    Datahub database
	 * @param tablename   Datahub table name
	 * @throws UnknownHostException if the database host cannot be resolved
	*/
	public DatahubDataModel(String host,
	                        String database,
	                        String username,
	                        String password,
	                        String tablename) throws UnknownHostException {
		this.datahubHost = host;
		this.datahubDatabase = database;
		this.datahubUsername = username;
		this.datahubPassword = password;
		this.datahubTableName = tablename;
		this.datahubOriginalTable = this.datahubTableName.split("_")[0];
		this.lastTimestamp = "";
		this.refreshed = false;
		this.random = false;
		this.ratingsColumn = null;
		this.itemRatings = false;
		this.item_count = 0;
		buildModel();
	}

	/**
	* Triggers "refresh" -- whatever that means -- of the implementation.
	* The general contract is that any should always leave itself in a
	* consistent, operational state, and that the refresh atomically updates
	* internal state from old to new.
	* </p>
	*
	* @param alreadyRefreshed s that are known to have already been refreshed as
	*                         a result of an initial call to a method on some object. This ensures
	*                         that objects in a refresh dependency graph aren't refreshed twice
	*                         needlessly.
	* @see #refreshData(String, Iterable, boolean)
	*/
	public void refresh(Collection<Refreshable> alreadyRefreshed) {
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ResultSet last;
			synchronized(this.client) {
				last = this.client.execute_sql(this.conn, "SELECT MAX(updated) FROM " + this.datahubDatabase + "." + this.datahubDatabase +"_" 
							+ this.datahubOriginalTable + "_update_log where table_name='" + this.datahubTableName + "'", null);
			}
			if(last != null) {
				for (Tuple t : last.getTuples()) {
					List<ByteBuffer> cells = t.getCells();
					String stamp = new String(cells.get(0).array());
					if (!this.lastTimestamp.equals(stamp) && !stamp.equals("None")) {
						//this.buildModel();
						this.lastTimestamp = stamp;
						this.refreshed = true;
						this.delegate.refresh(null);
					} else {
						this.refreshed = false;
					}
				}
			}
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void buildModel() throws UnknownHostException {
		// Map of user preferences by Mahout user id
		/*FastByIDMap<Collection<Preference>> userIDPrefMap = new FastByIDMap<Collection<Preference>>();
		System.out.println("Building model");*/
		
		try {
			this.transport = new THttpClient(this.datahubHost);
			this.protocol = new  TBinaryProtocol(transport);
			this.client = new DataHub.Client(protocol);
			System.out.println("Connected to Datahub Successfully");
		
			this.con_params = new ConnectionParams();
			this.con_params.setUser(this.datahubUsername);
			this.con_params.setPassword(this.datahubPassword);
			this.conn = this.client.open_connection(this.con_params);
			
			this.activeThreads = new HashMap<Integer, CreateItemSimilarityRunnable>();
			System.out.println("SELECT relname FROM pg_class WHERE relname = '" + this.datahubDatabase + "_" + this.datahubOriginalTable + "_update_log'");
			ResultSet updatelogExists =  this.client.execute_sql(this.conn, "SELECT relname FROM pg_class WHERE relname = '" + this.datahubDatabase + "_" + this.datahubOriginalTable + "_update_log'", null);
			if (updatelogExists != null && updatelogExists.getTuples().size() > 0) {
				ResultSet last = this.client.execute_sql(this.conn, "SELECT MAX(updated) FROM " + this.datahubDatabase + "." + this.datahubDatabase 
						+ "_" + this.datahubOriginalTable + "_update_log where table_name='" + this.datahubTableName + "'", null);
			
				if(last != null) {
					for (Tuple t : last.getTuples()) {
						List<ByteBuffer> cells = t.getCells();
						this.lastTimestamp = new String(cells.get(0).array());
					}
				}
				long startTime = System.nanoTime();
				this.delegate = new FileDataModel(new File(UpdateLocalFiles.getKibitzLocalStorageAddr() +  this.datahubUsername + "/" + this.datahubDatabase + "/" + this.datahubTableName + ".csv"));
				long endTime = System.nanoTime();
				System.out.println("Time it takes to retrieve items from file: " + (endTime - startTime));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Display a page of items
	 */
	public List<Item> getPageItems(String table, long page, long numPerPage, List<String> displayColumns) {
		try {
			return this.getListOfItems( "select kibitz_generated_id, " + StringUtils.join(displayColumns, ',') + " from " + 
					this.datahubDatabase + "." + table + " limit " + numPerPage + " offset " + numPerPage * page, displayColumns);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Counts the number of items in the database
	 */
	public int getItemCount(String table) {
		try {
			long startTime = System.nanoTime();
			ResultSet res;
			synchronized(this.client) {
				res = this.client.execute_sql(this.conn, "select count(*) from " + this.datahubDatabase + "." + table, null);
			}
			long endTime = System.nanoTime();
			long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
			System.out.println("Time getItemCount took to run: " + duration);
			
			for (Tuple t : res.getTuples()) {
				List<ByteBuffer> cells = t.getCells();
				return Integer.parseInt(new String(cells.get(0).array()));
			}
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Creates all associated tables associated with a new recommender.
	 */
	public boolean createNewRecommender(String table, String primaryKey, String firstColumnName, String secondColumnName, String thirdColumnName,
    		String firstColumnType, String secondColumnType, String thirdColumnType, List<String> displayColumns, String clientKey,
    		String ratingsColumn, boolean random) {
		if (ratingsColumn != null) {
			this.ratingsColumn = ratingsColumn;
		}
		
		if (random) {
			this.random = true;
		}
		
		if (firstColumnName != null) {
			this.itemRatings = true;
		}
		
		try {
			synchronized(this.client) {
				this.client.execute_sql(this.conn, "alter table " + this.datahubDatabase + "." + table + " drop column if exists kibitz_generated_id;"
										+ " alter table " + this.datahubDatabase + "." + table + " drop constraint if exists " + primaryKey + ";"
										+ " alter table " + this.datahubDatabase + "." + table + " add column kibitz_generated_id serial primary key;"
										+ " create table if not exists " + this.datahubDatabase + "." + table + "_ratings (" 
										+  "user_id int, item_id int, rating varchar(255));"
										+ " create table if not exists " + this.datahubDatabase + "." + table + "_users (" 
										+ "kibitz_generated_id SERIAL PRIMARY KEY, username varchar(255), email varchar(255), password varchar(255));"
										+ " CREATE TABLE if not exists " + this.datahubDatabase + "." + this.datahubDatabase +"_" + this.datahubOriginalTable + "_update_log(table_name text, updated timestamp NOT NULL DEFAULT now());" 
										+ "drop function if exists " + this.datahubUsername + "_" + this.datahubDatabase + "_" + table 
										+ "_timestamp_update_log() cascade; " + "CREATE FUNCTION " + this.datahubUsername + "_" + this.datahubDatabase + "_" 
										+ table + "_timestamp_update_log() RETURNS TRIGGER LANGUAGE 'plpgsql' AS $$" 
									    + "BEGIN INSERT INTO " + this.datahubDatabase + "." + this.datahubDatabase +"_" + this.datahubOriginalTable + "_update_log(table_name) VALUES(TG_TABLE_NAME); RETURN NEW;"
									    + "END $$; " + " drop trigger if exists " + table + "_timestamp_update_log on " + this.datahubDatabase + "." + table + "; "
										+ "CREATE TRIGGER " + table + "_timestamp_update_log "
										+ "AFTER INSERT OR UPDATE ON " + this.datahubDatabase + "." + table + " FOR EACH STATEMENT EXECUTE procedure "
										+ this.datahubUsername + "_" + this.datahubDatabase + "_" 
										+ table + "_timestamp_update_log();" + " drop trigger if exists " + table + "_ratings_timestamp_update_log on " 
										+ this.datahubDatabase + "." + table + "; CREATE TRIGGER " + table + "_ratings_timestamp_update_log "
										+ "AFTER INSERT OR UPDATE ON " + this.datahubDatabase + "." + table + "_ratings FOR EACH STATEMENT EXECUTE procedure "
										+ this.datahubUsername + "_" + this.datahubDatabase + "_" 
										+ table + "_timestamp_update_log();", null);
			}	
		
			String c = "select count(*) from pg_tables where schemaname = '" + this.datahubDatabase + "' and tablename = '" + table + "_item_combos';";
			ResultSet ifItemCombosExist;
			synchronized(this.client) {
				ifItemCombosExist = this.client.execute_sql(this.conn, c, null);
			}
			for (Tuple tt: ifItemCombosExist.getTuples()) {
				List<ByteBuffer> cs = tt.getCells();
				if ("0".equals(new String(cs.get(0).array()))) {
					System.out.println(new String(cs.get(0).array()));
					
					String query = "SELECT distinct p1.kibitz_generated_id AS firstid, p2.kibitz_generated_id AS secondid, ";
					
					if (firstColumnName != null) {
						query += "p1." + firstColumnName + " AS first1, p2." + firstColumnName + " AS second1";
					}
					
					if (secondColumnName != null) {
						query += ",p1." + secondColumnName + " AS first2, p2." + secondColumnName + " AS second2";
					}
					
					if (thirdColumnName != null) {
						query += ",p1." + thirdColumnName + " AS first3, p2." + thirdColumnName + " AS second3";
					}
					
					query += " INTO " + this.datahubDatabase + "." + table + "_item_combos_initial FROM " + this.datahubDatabase + "." + table 
							+ " AS p1, " + this.datahubDatabase + "." + table + " AS p2;";
					
					String initial_combo = "select count(*) from pg_tables where schemaname = '" + this.datahubDatabase + "' and tablename = '" + table + "_item_combos';";
					ResultSet ifInitialItemCombosExist;
					synchronized(this.client) {
						ifInitialItemCombosExist = this.client.execute_sql(this.conn, initial_combo, null);
					}
					
					for (Tuple initials: ifInitialItemCombosExist.getTuples()) {
						List<ByteBuffer> icombos = initials.getCells();
						if ("0".equals(new String(icombos.get(0).array()))) {
							this.client.execute_sql(this.conn, query, null);
						}
					}
					
					String newQuery = "select * into " + this.datahubDatabase + "." + table + "_item_combos from " + this.datahubDatabase + "." + table
							+ "_item_combos_initial order by random() limit 2000000";
					this.client.execute_sql(this.conn, newQuery, null);
				}
			}
			
			CreateItemSimilarityRunnable t;
			
			for (int i = 0; i < 10; i++) {
				t = new CreateItemSimilarityRunnable(i * 10000, (i+1)*10000, table, firstColumnName, secondColumnName, thirdColumnName);
				this.activeThreads.put(i*10000, t);
				t.run();
			}
			
			File direc = new File(UpdateLocalFiles.getKibitzLocalStorageAddr() + this.datahubUsername + "/" + this.datahubDatabase + "/homepage");
			if (direc.isDirectory()) {
				FileUtils.deleteDirectory(direc);
			}   
			
			FileUtils.copyDirectory(new File(UpdateLocalFiles.getKibitzLocalStorageAddr() + "base"), new File(UpdateLocalFiles.getKibitzLocalStorageAddr() 
					 + this.datahubUsername + "/" + this.datahubDatabase + "/homepage"));
			File login_creds = new File(UpdateLocalFiles.getKibitzLocalStorageAddr() + this.datahubUsername + "/" + this.datahubDatabase + "/homepage/required_functions.js");
			String input = "var title = '" + table + "';\n" + 
					"var client_key = '" + clientKey + "';\n" + 
					"\n" + "$(document).ready(function() {\n" + 
					"    transport.open();\n" + 
					"    client.createNewIndividualServer(client_key);\n" + 
					"    client.initiateModel('" + clientKey + "', '" + this.datahubTableName + "', '" 
					+ this.datahubUsername + "', '" + this.datahubPassword + "', '" + this.datahubDatabase + "');\n" + 
					"    document.getElementById(\"title\").innerHTML = title + ' Recommender';\n" + 
					"    $('#search').keyup(function(ev) {\n" + 
					"        if (ev.which === 13) {\n" + 
					"            var items = client.getSearchItems(client_key, $('#search').val());\n" + 
					"\n" + 
					"            document.getElementById('listofitems').innerHTML = \"\";\n" + 
					"            var itemslist =\"\";\n" + 
					"            for (var i =0; i < items.length; i++) {\n" + 
					"                var item = items[i];\n" + 
					"                if (item.id != null && item.title != null) {\n" + 
					"                    currItem = '<tr><tr><div class=\"relative\">';\n" + 
					"                    if (item.image.indexOf('http') > -1) {\n" + 
					"                        currItem += '<img src = \"' + item.image + '\" />';\n" + 
					"                    }\n" + 
					"                    currItem += '<div class=\"inline-block user-info\"><h2>' + item.title + '</h2>';\n" + 
					"                    if (item.description != null){\n" + 
					"                        currItem += '<div class=\"icons\"><ul class=\"list-inline\"><li>' + item.description + '</li>';\n" + 
					"                    }\n" + 
					"                    currItem += '<div id=\"rate' + item.id + '\" class=\"rating\">&nbsp;</div><div class=\"implementation\"></div>';\n" + 
					"                    if (item.description != null) {\n" + 
					"                        currItem += '</ul></div>';\n" + 
					"                    }\n" + 
					"                    currItem += '</div></div></td></tr>';\n" + 
					"                                                                                                                    itemslist += currItem;\n" + 
					"                                                                                                                          }\n" + 
					"                                                }\n" + 
					"                            document.getElementById('listofitems').innerHTML = itemslist;\n" + 
					"\n" + 
					"                                if(userId != null) {\n" + 
					"                                                var my_rated_items = client.getUserRatedItems(client_key, userId);\n" + 
					"                                                        for (i = 0; i < my_rated_items.length; i++) {\n" + 
					"                                                                            item = my_rated_items[i];\n" + 
					"                                                                                        var r = document.getElementById('rate' + item.id);\n" + 
					"                                                                                                    if (r != null) {\n" + 
					"                                                                                                                            r.setAttribute('value', item.rating ? item.rating: -1);\n" + 
					"                                                                                                                                        }\n" + 
					"                                                                                                            }\n" + 
					"                                                            }\n" + 
					"                                    var ratings = $('.rating');\n" + 
					"                                        ratings.each(function (i, el) {\n" + 
					"                                                        var rating = parseInt($(el).attr('value'));\n" + 
					"                                                                if (rating > -1) {\n" + 
					"                                                                                    $(el).rating('', {maxvalue: 10, curvalue: rating});\n" + 
					"                                                                                            } else {\n" + 
					"                                                                                                                $(el).rating('', {maxvalue: 10});\n" + 
					"                                                                                                                        }\n" + 
					"        });\n" + 
					"        }\n" + 
					"    });\n" + 
					"});";
			FileWriter fileWriter = new FileWriter(login_creds.getAbsolutePath(), true);
	        BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
	        bufferWriter.write(input);
	        bufferWriter.close();
	        
	        ZipUtil.pack(new File(UpdateLocalFiles.getKibitzLocalStorageAddr() 
					 + this.datahubUsername + "/" + this.datahubDatabase + "/homepage"), new File(UpdateLocalFiles.getKibitzLocalStorageAddr() 
							 + this.datahubUsername + "/" + this.datahubDatabase + "/homepage.zip"));
	        
	        direc = new File(WEBSERVER_DIR + this.datahubUsername + "/" + this.datahubDatabase);
			if (direc.isDirectory()) {
				FileUtils.deleteDirectory(direc);
			}   
			
			FileUtils.copyDirectory(new File(UpdateLocalFiles.getKibitzLocalStorageAddr() + this.datahubUsername + "/" + this.datahubDatabase + "/homepage"), new File(WEBSERVER_DIR
					 + this.datahubUsername + "/" + this.datahubDatabase));
			FileUtils.copyFile(new File(UpdateLocalFiles.getKibitzLocalStorageAddr() 
							 + this.datahubUsername + "/" + this.datahubDatabase + "/homepage.zip"), 
							 new File(WEBSERVER_DIR + this.datahubUsername + "/" + this.datahubDatabase + "/homepage.zip"));
			
			File ratings = new File(UpdateLocalFiles.getKibitzLocalStorageAddr() + this.datahubUsername + "/" + this.datahubDatabase + "/" + this.datahubOriginalTable + "_ratings.csv");
			ratings.createNewFile();
			
			fileWriter = new FileWriter(ratings.getAbsolutePath(), true);
	        bufferWriter = new BufferedWriter(fileWriter);
	        bufferWriter.write("1,1,1");
	        bufferWriter.close();
	        
	        File item_sims = new File(UpdateLocalFiles.getKibitzLocalStorageAddr() + this.datahubUsername + "/" + this.datahubDatabase + "/" + this.datahubOriginalTable + "_item_similarity.csv");
			item_sims.createNewFile();
			
			boolean rate = false;
			if (this.ratingsColumn != null) {
				rate = true;
			}
			this.addKibitzRecommender(this.datahubDatabase, this.datahubUsername, this.datahubPassword, this.datahubDatabase + "." + table, this.itemRatings, this.random, rate, ratingsColumn);
			return true;
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public List<Item> makeOverallRatingsBasedRecommendation(String table, long numRecs, List<String> displayColumns) {
		try {
			String ratingsColumnName = null;
			
			THttpClient transport = new THttpClient(this.datahubHost);
			TBinaryProtocol protocol = new  TBinaryProtocol(transport);
			DataHub.Client clnt = new DataHub.Client(protocol);
	
			ConnectionParams params = new ConnectionParams();
			params.setUser(DatahubDataModel.getDefaultDatahubUsername());
			params.setPassword(DatahubDataModel.getDefaultDatahubPassword());
			Connection connection = clnt.open_connection(params);
			
			ResultSet res = clnt.execute_sql(connection, "SELECT ratings_column FROM kibitz_users.recommenders WHERE username = '" + this.datahubUsername
					+ "' AND database = '" + this.datahubDatabase + "' AND password = '" + this.datahubPassword + "'", null);
			HashMap<String, Integer> colToIndex = this.getFieldNames(res);			
			for (Tuple t : res.getTuples()) {
				List<ByteBuffer> cells = t.getCells();
				ratingsColumnName = new String(cells.get(colToIndex.get("ratings_column")).array());
			}
			
			if (ratingsColumnName != null) {
				return this.getListOfItems("select kibitz_generated_id, "+ StringUtils.join(displayColumns, ',') + " from " + table
					+ " ORDER BY " + ratingsColumnName + " DESC LIMIT " + numRecs, displayColumns);
			}
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Item> makeRandomRecommmendation(long numRecs, String table, List<String> displayColumns) {
		try {
			return this.getListOfItems("select kibitz_generated_id, " + StringUtils.join(displayColumns, ',') + " from " + table
					+ " ORDER BY RANDOM() LIMIT " + numRecs, displayColumns);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private double calculatePhraseSimilarity(String first, String second) throws IllegalStateException, IOException {
		WS4JConfiguration.getInstance().setMFS(true);

		double score = 0.0;
		for ( RelatednessCalculator rc : rcs ) {
			double[][] r = rc.getSimilarityMatrix(first.split(" "), second.split(" "));
            
            double relatednessScore = 0.0;
            int relatedWordsCount = 0;
            for (int i = 0; i < r.length; i++) {
            	for (int j = 0; j < r[0].length; j++) {
            		if (r[i][j] > -1 && r[i][j] <= 1 && r[i][j] != 0) {
            			relatednessScore += r[i][j];
            			relatedWordsCount += 1;
            		}
            	}
            }
            
            if (relatedWordsCount > 0) {
            	//System.out.println(relatednessScore / relatedWordsCount);
            	score += relatednessScore / relatedWordsCount;
            }
		}
		
		//System.out.println(score / 4);
		return score / 4;
	}
	
	/**
	 * Gets list of items user has rated.
	 */
	 public List<Item> getUserRatedItems(long userId, String ratings_table, String items_table, List<String> displayColumns) {
		try {
			List<String> itemsTableColumns = new ArrayList<String>();
			for (String name: displayColumns) {
				itemsTableColumns.add(items_table + "." + name);
			}
			
			displayColumns.add("rating");
			
			return this.getListOfItems("SELECT " + ratings_table + ".item_id as kibitz_generated_id, " + ratings_table + 
					".rating, " + StringUtils.join(itemsTableColumns, ',') + ", " + ratings_table + ".user_id FROM " + ratings_table + " INNER JOIN " + items_table + 
					" ON " + ratings_table + ".item_id = " + items_table + ".kibitz_generated_id" + " WHERE user_id=" + userId, displayColumns);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<Item>();
	}
	 
	 /**
	  * Gets list of ids of items rated
	  */
	 public long[] getUserRatedItemsIds(long userId, String ratings_table) {
		 List<Long> userRatedItemsIds = new ArrayList<Long>();
		 try {
			ResultSet res;
			synchronized(this.client) {
				res = this.client.execute_sql(this.conn, "SELECT item_id FROM " + ratings_table +
						" WHERE user_id=" + userId, null);
			}
			HashMap<String, Integer> colToIndex = this.getFieldNames(res);

			for (Tuple t : res.getTuples()) {
				List<ByteBuffer> cells = t.getCells();
				userRatedItemsIds.add(Long.parseLong(new String(cells.get(colToIndex.get("item_id")).array())));
			}
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Long[] ids = new Long[userRatedItemsIds.size()];
		return ArrayUtils.toPrimitive(userRatedItemsIds.toArray(ids));
	 }
	 
	/**
	 * Records user ratings
	 */
	 public void recordRatings(long userId, long itemId, long rating, String ratings_table) {
		List<Long> columns = new ArrayList<Long>();
		columns.add(userId);
		columns.add(itemId);
		columns.add(rating);
		List<String> columnNames = new ArrayList<String>();
		columnNames.add("user_id");
		columnNames.add("item_id");
		columnNames.add("rating");
		long startTime = System.nanoTime();
		if (!saveIntoDb(columns, null , columnNames, ratings_table)) {
			this.deleteRatings(userId, itemId, ratings_table);
			this.saveIntoDb(columns, null, columnNames, ratings_table);
		}
		long finishDBTime = System.nanoTime();
		this.writeNewRatings(userId, itemId, rating);
		long finishRecordTime = System.nanoTime();
		System.out.println("Time saveIntoDb took: " + (finishDBTime - startTime));
		System.out.println("Time writeNewRatings took: " + (finishRecordTime - startTime));
	 }
	 
	 /**
	  * Gets user ID
	  */
	 public long retrieveUserId(String username, String password, String table) {
		 try {
			 ResultSet res;
			 synchronized(this.client) {
				 res = this.client.execute_sql(this.conn, "SELECT kibitz_generated_id,password FROM " + table + " WHERE username = '" + username + "'", null);
			 }
			 HashMap<String, Integer> colToIndex = this.getFieldNames(res);

			 for (Tuple t : res.getTuples()) {
				List<ByteBuffer> cells = t.getCells();
				long id = Long.parseLong(new String(cells.get(colToIndex.get("kibitz_generated_id")).array()));
				String storedPassword = new String(cells.get(colToIndex.get("password")).array());
				try {
					if (IndividualRecommender.validatePassword(password, storedPassword)) {
						return id;
					}
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidKeySpecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	 }
	 
	 /**
	  * Deletes user ratings.
	  */
	 public void deleteRatings(long userId, long itemId, String table) {
		 try {
			  synchronized(this.client) {
			 	this.client.execute_sql(this.conn, "DELETE FROM " + table + " WHERE user_id = " + userId + " AND "
			 			+ "item_id = " + itemId, null);
			  }
			 	//this.delegate.removePreference(userId, itemId);
		 } catch (DBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		 } catch (TException e) {
			 	// TODO Auto-generated catch block
			 	e.printStackTrace();
		 } /*catch (TasteException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }*/
	 }
	 
	 public List<Item> getItemsFromPrimaryKeys(String primaryKey, List<String> itemKeys, List<String> displayColumns, String table) {
		 String query = "select " + StringUtils.join(displayColumns, ", ") + " from " + table + " where " 
				 + primaryKey + "=";
		 for (int i = 0; i < itemKeys.size() - 1; i++) {
			 query += itemKeys.get(i) + " or " + primaryKey + "=";
		 }
		 query += itemKeys.get(itemKeys.size() - 1);
		 try {
			return this.getListOfItems(query, displayColumns);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	 }
	 
	 public void createNewUser(List<?> columns, String suffix, List<String>columnNames, String table, boolean isCentralRepo) {
		 if(isCentralRepo) {
			 saveIntoDb(columns, suffix, columnNames, DatahubDataModel.getDefaultDatahubTablename());
		 } else {
			 saveIntoDb(columns, suffix, columnNames, table);
		 }
	 }
	 /**
	  * Saves record into Datahub.
	  */
	 private boolean saveIntoDb(List<?> columns, String suffix, List<String>columnNames, String table) {
			try {
				String q = "SELECT COUNT(*) FROM " + table + " WHERE ";
				for (int i = 0; i < columns.size(); i++) {
					if (columnNames.get(i) != "rating") {
						q = q + columnNames.get(i) + "=";
						if (columns.get(i).getClass().getName() != "java.lang.Integer") {
							q = q + columns.get(i) + " AND ";
						} else {
							q = q + columns.get(i) + " AND ";
						}
					}
				}
				q = q.substring(0, q.length() - 5);
				ResultSet res;
				synchronized(this.client) {
					res = this.client.execute_sql(this.conn, q, null);
				}
				for (Tuple t : res.getTuples()) {
					List<ByteBuffer> cells = t.getCells();
					int num = Integer.parseInt(new String((cells.get(0).array())));
					if (num == 0) {
						synchronized(this.client) {
							this.client.execute_sql(this.conn, "INSERT INTO " + table + "(" + StringUtils.join(columnNames, ",") + ") VALUES (" + StringUtils.join(columns, ",") + ");", null);
						}
						return true;
					}
				}
			} catch (DBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
	 }
	 
	 public String checkLogin(String username, String password, String table, boolean isCentralRepo) {
		 try {
		 	if (isCentralRepo) {
				THttpClient transport = new THttpClient(this.datahubHost);
				TBinaryProtocol protocol = new  TBinaryProtocol(transport);
				DataHub.Client client = new DataHub.Client(protocol);
			
				ConnectionParams params = new ConnectionParams();
				params.setUser(DatahubDataModel.getDefaultDatahubUsername());
				params.setPassword(DatahubDataModel.getDefaultDatahubPassword());
				Connection connection = client.open_connection(params);
				
				ResultSet res = client.execute_sql(connection, "SELECT password FROM " + DatahubDataModel.getDefaultDatahubTablename() + " WHERE username='" + username + "'", null);
				for (Tuple t : res.getTuples()) {
					List<ByteBuffer> cells = t.getCells();
					String hash = new String((cells.get(0).array()));
					return hash;
				}
			 } else {
				ResultSet res;
				 
				synchronized(this.client) {
					res = this.client.execute_sql(this.conn, "SELECT password FROM " + table + " WHERE username='" + username + "'", null);
				}
				for (Tuple t : res.getTuples()) {
					List<ByteBuffer> cells = t.getCells();
					String hash = new String((cells.get(0).array()));
					return hash;
				}
			 }
		 } catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return null;
	 }
	 
	 /**
	  * Checks whether username is already taken.
	  * @param username
	  * @return
	  */
	 public boolean checkUsername(String username, String table, boolean isCentralRepo) {
			try {	
				if (isCentralRepo) {
					THttpClient transport = new THttpClient(this.datahubHost);
					TBinaryProtocol protocol = new  TBinaryProtocol(transport);
					DataHub.Client client = new DataHub.Client(protocol);
				
					ConnectionParams params = new ConnectionParams();
					params.setUser(DatahubDataModel.getDefaultDatahubUsername());
					params.setPassword(DatahubDataModel.getDefaultDatahubPassword());
					Connection connection = client.open_connection(params);
					
					System.out.println( "SELECT COUNT(*) FROM kibitz_users." + DatahubDataModel.getDefaultDatahubTablename() + " WHERE email='" + username + "'");
					ResultSet res = client.execute_sql(connection, "SELECT COUNT(*) FROM kibitz_users." + DatahubDataModel.getDefaultDatahubTablename() + " WHERE email='" + username + "'", null);
					for (Tuple t : res.getTuples()) {
						List<ByteBuffer> cells = t.getCells();
						int num = Integer.parseInt(new String((cells.get(0).array())));
						if (num == 0) {
							return false;
						} else {
							return true;
						}
					}
				} else {
					ResultSet res;
					synchronized(this.client) {
						res = this.client.execute_sql(this.conn, "SELECT COUNT(*) FROM " + table + " WHERE username='" + username + "'", null);
					}
					for (Tuple t : res.getTuples()) {
						List<ByteBuffer> cells = t.getCells();
						int num = Integer.parseInt(new String((cells.get(0).array())));
						if (num == 0) {
							return false;
						} else {
							return true;
						}
					}
				}
			} catch (TTransportException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
	 }
	
	/**
	 * Maps id from Datahub to long for Mahout recommenders
	 */
	public String fromIdToLong(String id, boolean isUser) {
		// creates a map of the id to the long value
		return id;
	}
	
	/**
	 * Gets list of items from list of ids
	 */
	public List<Item> getItemsFromIds(ArrayList<Long> ids, String table, String ratings_table, long userId, List<String> displayColumns) {
		List<Item> items = new ArrayList<Item>();
		
		List<String> itemsTableColumns = new ArrayList<String>();
		for (String name: displayColumns) {
			itemsTableColumns.add(table + "." + name);
		}
		
		displayColumns.add("rating");
		
		try {			
			synchronized(this.client) {
				if(ids.size() > 0) {
					String query = "SELECT " + table + ".kibitz_generated_id, " + StringUtils.join(displayColumns, ",") 
						+ ratings_table + ".rating FROM " + table + " LEFT JOIN " + ratings_table + " ON " + ratings_table 
						+ ".item_id=" + table + ".kibitz_generated_id" + " AND user_id=" + userId + " WHERE (kibitz_generated_id='";
					for (int i = 0; i < ids.size() - 1; i++) {
						query += ids.get(i) + "' OR kibitz_generated_id='";
					}
					query += ids.get(ids.size() - 1) + "')";
					return this.getListOfItems(query, displayColumns);
				} else {
					return new ArrayList<Item>();
				}
			}
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return items;
	}
	
	/**
	 * Gets item from item id
	 */
	public Item getItemFromId(long id, String table, List<String> displayColumns) {
		try {
			HashMap<String, String> attributes = new HashMap<String, String>();
			ResultSet res;
			
			synchronized(this.client) {
				res = this.client.execute_sql(this.conn, "SELECT kibitz_generated_id, " + StringUtils.join(displayColumns, ",") + " FROM " + table 
						+ " WHERE kibitz_generated_id='" + id + "'", null);
			}
			HashMap<String, Integer> colToIndex = this.getFieldNames(res);
			
			for (Tuple t : res.getTuples()) {
				List<ByteBuffer> cells = t.getCells();
				Item item = new Item();
				item.setId(Long.parseLong(new String(cells.get(colToIndex.get("kibitz_generated_id")).array())));
				
				for (String column: displayColumns) {
					if (colToIndex.containsKey(column) && !new String(cells.get(colToIndex.get(column)).array()).equals("None"))
						attributes.put(column, new String(cells.get(colToIndex.get(column)).array()));
				}
				
				item.setAttributes(attributes);
				return item;
			}
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
 	
	/**
	 * Return search results
	 */
	public List<Item> getSearchItems(String table, String query, List<String> displayColumns) {
		List<Item> items = new ArrayList<Item>();
		try {
			String q = "select kibitz_generated_id, " + StringUtils.join(displayColumns, ",") + " from " + table + " where ";
			String[] words = query.split(" ");

			List<String> displayQueries = new ArrayList<String>();
			for (String column: displayColumns) {
				String qry = "(" + column + " like ";
				for (int i = 0; i < words.length - 1; i++) {
					q += "'%" + words[i] + "%' and " + column + " like ";
				}
				q += "'%" + words[words.length - 1] + "%')";
				displayQueries.add(qry);
			}
			q += StringUtils.join(displayQueries, " OR ");
			return this.getListOfItems(q, displayColumns);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return items;
	}
	
	/**
	 * Maps Mahout long value to Datahub ID
	 */
	public String fromLongToId(long id) {
		return new String();
	}
	
	public boolean getRefreshed() {
		return this.refreshed;
	}
	
	/**
	 * Returns last timestamp; used to terminate old recommenders
	 */
	public String getTimestamp() {
		return this.lastTimestamp;
	}
	
	/**
	 * Adds Kibitz user to database
	 */
	public void addKibitzUser(String email, String password) {
		try {
			THttpClient transport = new THttpClient(this.datahubHost);
			TBinaryProtocol protocol = new  TBinaryProtocol(transport);
			DataHub.Client clnt = new DataHub.Client(protocol);
	
			ConnectionParams params = new ConnectionParams();
			params.setUser(DatahubDataModel.getDefaultDatahubUsername());
			params.setPassword(DatahubDataModel.getDefaultDatahubPassword());
			Connection connection = clnt.open_connection(params);
			
			clnt.execute_sql(connection, "INSERT INTO kibitz_users.users (email, password) "
					+ " VALUES ('" + email + "', '" + IndividualRecommender.generatePasswordHash(password) + "');", null);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Adds this Kibitz recommender to database
	 */
	public void addKibitzRecommender(String databaseName, String username, String password, String ratings_table, 
			boolean itemSimilarity, boolean random, boolean rate, String ratingsColumn) {
		try {
			THttpClient transport = new THttpClient(this.datahubHost);
			TBinaryProtocol protocol = new  TBinaryProtocol(transport);
			DataHub.Client clnt = new DataHub.Client(protocol);
	
			ConnectionParams params = new ConnectionParams();
			params.setUser(DatahubDataModel.getDefaultDatahubUsername());
			params.setPassword(DatahubDataModel.getDefaultDatahubPassword());
			Connection connection = clnt.open_connection(params);
			
			ResultSet res = client.execute_sql(connection, "SELECT COUNT(*) FROM kibitz_users.recommenders WHERE database='" + databaseName 
					+ "' AND username = '" + username + "';", null);
			for (Tuple t : res.getTuples()) {
				List<ByteBuffer> cells = t.getCells();
				int num = Integer.parseInt(new String((cells.get(0).array())));
				if (num == 0) {
					clnt.execute_sql(connection, "INSERT INTO kibitz_users.recommenders (database, username, password, ratings_table, item_sim, random, overall_ratings, ratings_column, active) "
							+ " VALUES ('" + databaseName + "', '" + username + "', '" + password + "', '" + ratings_table + "'," + itemSimilarity + "," + random 
							+ "," + rate + ", '" + ratingsColumn + "', false)", null);
					return;
				}
			}
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the types of recommenders
	 * @param res
	 * @return
	 */
	public List<Boolean> getRecommenderTypes() {
		try {
			THttpClient transport = new THttpClient(this.datahubHost);
			TBinaryProtocol protocol = new  TBinaryProtocol(transport);
			DataHub.Client clnt = new DataHub.Client(protocol);
	
			ConnectionParams params = new ConnectionParams();
			params.setUser(DatahubDataModel.getDefaultDatahubUsername());
			params.setPassword(DatahubDataModel.getDefaultDatahubPassword());
			Connection connection = clnt.open_connection(params);
			
			System.out.println("SELECT item_sim, random, overall_ratings FROM kibitz_users.recommenders WHERE username = '" + this.datahubUsername
					+ "' AND database = '" + this.datahubDatabase + "' AND password = '" + this.datahubPassword + "'");
			ResultSet res = clnt.execute_sql(connection, "SELECT item_sim, random, overall_ratings FROM kibitz_users.recommenders WHERE username = '" + this.datahubUsername
					+ "' AND database = '" + this.datahubDatabase + "' AND password = '" + this.datahubPassword + "'", null);
			HashMap<String, Integer> colToIndex = this.getFieldNames(res);
			List<Boolean> recs = new ArrayList<Boolean>();
			
			for (Tuple t : res.getTuples()) {
				List<ByteBuffer> cells = t.getCells();
				recs.add(Boolean.parseBoolean(new String(cells.get(colToIndex.get("item_sim")).array())));
				recs.add(Boolean.parseBoolean(new String(cells.get(colToIndex.get("random")).array())));
				recs.add(Boolean.parseBoolean(new String(cells.get(colToIndex.get("overall_ratings")).array())));
				return recs;
			}
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private HashMap<String, Integer> getFieldNames(ResultSet res) {
		List<String> fieldNames = res.getField_names();
		HashMap<String, Integer> colToIndex = new HashMap<String, Integer>();
		for (int i = 0; i < fieldNames.size(); i++) {
			colToIndex.put(fieldNames.get(i), i);
	    }
		return colToIndex;
	}
	
	/**
	 * Writes newest preferences to delta files
	 */
	private void writeNewRatings(long userId, long itemId, long rating) {
		try {
		    FileWriter fw = new FileWriter(UpdateLocalFiles.getKibitzLocalStorageAddr() + 
		    		 this.datahubUsername + "/" + this.datahubDatabase + "/" + this.datahubTableName + ".update.csv",true);
		    if (rating == -1) 
		    	fw.write(userId + "," + itemId + ", \n");
		    else 
		    	fw.write(userId + "," + itemId + "," + rating + "\n");
		    fw.close();
		} catch(IOException e) {
		    System.err.println("IOException: " + e.getMessage());
		}
	}
	
	/**
	 * Returns list of Item objects from query
	 * @throws TException 
	 * @throws DBException 
	 */
	public List<Item> getListOfItems(String query, List<String> displayColumns) throws DBException, TException {
		List<Item> items = new ArrayList<Item>();
		HashMap<String, String> attributes = new HashMap<String, String>();
		ResultSet res;
		System.out.println(query);
		synchronized(this.client) {
			res = this.client.execute_sql(this.conn, query, null);
		}
		HashMap<String, Integer> colToIndex = this.getFieldNames(res);
		
		for (Tuple t : res.getTuples()) {
			List<ByteBuffer> cells = t.getCells();
			Item item = new Item();
			item.setId(Long.parseLong(new String(cells.get(colToIndex.get("kibitz_generated_id")).array())));
			
			attributes = new HashMap<String, String>();
			for (String column: displayColumns) {
				if (colToIndex.containsKey(column) && !new String(cells.get(colToIndex.get(column)).array()).equals("None"))
					attributes.put(column, new String(cells.get(colToIndex.get(column)).array()));
			}
			
			item.setAttributes(attributes);
			items.add(item);
		}
		return items;
	}
	
	public String getRatingsColumn() {
		return this.ratingsColumn;
	}
	
	public boolean getItemBased() {
		return this.itemRatings;
	}
	
	public boolean getRandom() {
		return this.random;
	}
 
	/**
	* Cleanup mapping collection.
	*/
	public void cleanupMappingCollection() {
	}
	
	@Override
	public LongPrimitiveIterator getUserIDs() throws TasteException {
		return delegate.getUserIDs();
	}

	@Override
	public PreferenceArray getPreferencesFromUser(long id) throws TasteException {
		return delegate.getPreferencesFromUser(id);
	}
	
	@Override
	public FastIDSet getItemIDsFromUser(long userID) throws TasteException {
		return delegate.getItemIDsFromUser(userID);
	}

	@Override
	public LongPrimitiveIterator getItemIDs() throws TasteException {
		return delegate.getItemIDs();
	}

	@Override
	public PreferenceArray getPreferencesForItem(long itemID) throws TasteException {
	    return delegate.getPreferencesForItem(itemID);
	}

	@Override
	public Float getPreferenceValue(long userID, long itemID) throws TasteException {
	    return delegate.getPreferenceValue(userID, itemID);
	}

	@Override
	public Long getPreferenceTime(long userID, long itemID) throws TasteException {
	    return delegate.getPreferenceTime(userID, itemID);
	}

	@Override
	public int getNumItems() throws TasteException {
	    return delegate.getNumItems();
	}

	@Override
	public int getNumUsers() throws TasteException {
	    return delegate.getNumUsers();
	}

	@Override
	public int getNumUsersWithPreferenceFor(long itemID) throws TasteException {
	    return delegate.getNumUsersWithPreferenceFor(itemID);
	}

	@Override
	public int getNumUsersWithPreferenceFor(long itemID1, long itemID2) throws TasteException {
	    return delegate.getNumUsersWithPreferenceFor(itemID1, itemID2);
	}

	@Override
	public void setPreference(long userID, long itemID, float value) {
	    throw new UnsupportedOperationException();
	}

	@Override
	public void removePreference(long userID, long itemID) {
	    throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasPreferenceValues() {
	    return delegate.hasPreferenceValues();
	}

	@Override
	public float getMaxPreference() {
	    return delegate.getMaxPreference();
	}

	@Override
	public float getMinPreference() {
	    return delegate.getMinPreference();
	}

	public static String getDefaultDatahubUsername() {
		return DEFAULT_DATAHUB_USERNAME;
	}

	public static String getDefaultDatahubPassword() {
		return DEFAULT_DATAHUB_PASSWORD;
	}

	public static String getDefaultDatahubTablename() {
		return DEFAULT_DATAHUB_TABLENAME;
	}

	public static String getDefaultDatahubHost() {
		return DEFAULT_DATAHUB_HOST;
	}
	
	public class CreateItemSimilarityRunnable implements Runnable {	
		private int upperBound;
		private String table;
		private String firstColumnName;
		private String secondColumnName;
		private String thirdColumnName;
		
		private BufferedWriter writer = null;
		private File file = null;
		private int lowerBound;
		
		public CreateItemSimilarityRunnable(int lowerBound, int upperBound, String table, String firstColumnName, String secondColumnName, 
				String thirdColumnName) {
			this.upperBound = upperBound;
			this.lowerBound = lowerBound;
			this.table = table;
			this.firstColumnName = firstColumnName;
			this.secondColumnName = secondColumnName;
			this.thirdColumnName = thirdColumnName;
		}
		
		public void run() {
			this.writeToItemSimilarityFile();
		}
		
		private void writeToItemSimilarityFile() {
			try {
				double firstColumnScore = 0;
				double secondColumnScore = 0;
				double thirdColumnScore = 0;
				
				long startTime = 0;
				
				for (int i = this.lowerBound; i < this.upperBound; i+= 10000) {
					long endTime = System.currentTimeMillis();
					String item_combos = "SELECT * FROM " + DatahubDataModel.this.datahubDatabase + "." + table + "_item_combos LIMIT 10000 OFFSET " + i;
					
					System.out.println("Time it takes to process 10000 items: " + ((float) (endTime - startTime))/(10*10*10*60));
					startTime = System.currentTimeMillis();
					
					ResultSet res;
					synchronized(DatahubDataModel.this.client) {
							res = DatahubDataModel.this.client.execute_sql(DatahubDataModel.this.conn, item_combos, null);
					}
					HashMap<String, Integer> colToIndex = DatahubDataModel.this.getFieldNames(res);
				
					for (Tuple t : res.getTuples()) {
						firstColumnScore = 0;
						secondColumnScore = 0;
						thirdColumnScore = 0;
						List<ByteBuffer> cells = t.getCells();
						if (firstColumnName != null) {
							String first1 = new String(cells.get(colToIndex.get("first1")).array());
							String second1 = new String(cells.get(colToIndex.get("second1")).array());
							firstColumnScore = DatahubDataModel.this.calculatePhraseSimilarity(first1, second1);
						}
						
						if (secondColumnName != null) {
							String first2 = new String(cells.get(colToIndex.get("first2")).array());
							String second2 = new String(cells.get(colToIndex.get("second2")).array());
							secondColumnScore = DatahubDataModel.this.calculatePhraseSimilarity(first2, second2);
						}
						
						if (thirdColumnName != null) {
							String first3 = new String(cells.get(colToIndex.get("first3")).array());
							String second3 = new String(cells.get(colToIndex.get("second3")).array());
							thirdColumnScore = DatahubDataModel.this.calculatePhraseSimilarity(first3, second3);
						}
						
						this.writeSimilarityScore(table, new String(cells.get(colToIndex.get("firstid")).array()), 
								new String(cells.get(colToIndex.get("secondid")).array()), (float) (0.5*firstColumnScore + 0.3*secondColumnScore + 0.2*thirdColumnScore));
					}
				}
				
				if (this.writer != null) 
					this.writer.close();
				this.completedThread();
				
				DatahubDataModel.this.checkCompletion(this.table);
			} catch (DBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private void writeSimilarityScore(String table, String firstId, String secondId, float score) {
			try {	
				if (this.file == null || this.writer == null) {
					String storageDir = UpdateLocalFiles.getKibitzLocalStorageAddr();
					this.file = new File(storageDir + DatahubDataModel.this.datahubUsername);
					this.file.mkdir();
					this.file = new File(storageDir +  DatahubDataModel.this.datahubUsername + "/" + DatahubDataModel.this.datahubDatabase);
					this.file.mkdir();
					this.file = new File(storageDir + DatahubDataModel.this.datahubUsername + "/" + DatahubDataModel.this.datahubDatabase + "/" + table + "_item_similarity" 
							+ "." + this.lowerBound + "_" + this.upperBound + ".csv");
					this.file.createNewFile();
					this.writer  = new BufferedWriter(new FileWriter(storageDir + DatahubDataModel.this.datahubUsername + "/" 
							+ DatahubDataModel.this.datahubDatabase + "/" + table + "_item_similarity" + "." + this.lowerBound + "_" + this.upperBound + ".csv"));
				}
				this.writer.write(firstId + "," + secondId + "," + (double) Math.round(score * 1000) / 1000 + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private void completedThread() {
			DatahubDataModel.this.activeThreads.remove(this.lowerBound);
		}
	}

	public void checkCompletion(String table) {
		if (this.activeThreads.size() <= 0 && this.item_count == 0) {
			try {
				THttpClient transport = new THttpClient(this.datahubHost);
				TBinaryProtocol protocol = new  TBinaryProtocol(transport);
				DataHub.Client clnt = new DataHub.Client(protocol);
		
				ConnectionParams params = new ConnectionParams();
				params.setUser(DatahubDataModel.getDefaultDatahubUsername());
				params.setPassword(DatahubDataModel.getDefaultDatahubPassword());
				Connection connection = clnt.open_connection(params);
				
				clnt.execute_sql(connection, "UPDATE kibitz_users.recommenders SET active = true WHERE " + "username = '" + this.datahubUsername
						+ "' AND database = '" + this.datahubDatabase  + "';", null);
			} catch (DBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (int i = 0; i < 10; i++) {
				File f1 = new File(UpdateLocalFiles.getKibitzLocalStorageAddr() + DatahubDataModel.this.datahubUsername + "/" 
						+ DatahubDataModel.this.datahubDatabase + "/" + table + "_item_similarity" + "." + i * 10000 + "_" + (i+1)*10000 + ".csv");
				File f2 = new File(UpdateLocalFiles.getKibitzLocalStorageAddr() + DatahubDataModel.this.datahubUsername + "/" 
						+ DatahubDataModel.this.datahubDatabase + "/" + table + "_item_similarity.csv");
				
				try {
					InputStream in = new FileInputStream(f1);
					OutputStream out = new FileOutputStream(f2, true);
					try {
						IOUtils.copy(in, out);
					} finally {
					    IOUtils.closeQuietly(in);
					    IOUtils.closeQuietly(out);
					}
					this.item_count++;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("THIS PROCESS HAS COMPLETED!");
		}
	}
}
