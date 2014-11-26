package kibitz;

import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.PlusAnonymousConcurrentUserDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import datahub.*;

import com.google.common.collect.Lists;

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
	
	private String datahubHost = DEFAULT_DATAHUB_HOST;
	private String datahubUsername = getDefaultDatahubUsername();
	private String datahubPassword = getDefaultDatahubPassword();
	private String datahubDatabase = DEFAULT_DATAHUB_DATABASE;
	private String datahubTableName = getDefaultDatahubTablename();
	private GenericDataModel generalModel = null;
	private String lastTimestamp;
	private boolean refreshed;
	
	private TTransport transport;
	private TProtocol protocol;
	private DataHub.Client client;
	private ConnectionParams con_params;
	private Connection conn;

	private PlusAnonymousConcurrentUserDataModel delegate;

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
		this.lastTimestamp = "";
		this.refreshed = false;
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
			ResultSet last;
			synchronized(this.client) {
				last = this.client.execute_sql(this.conn, "SELECT MAX(updated) FROM " + this.datahubDatabase + "." + "update_log where table_name='" + this.datahubTableName + "'", null);
			}
			if(last != null) {
				for (Tuple t : last.getTuples()) {
					List<ByteBuffer> cells = t.getCells();
					String stamp = new String(cells.get(0).array());
					if (!this.lastTimestamp.equals(stamp) && !stamp.equals("None")) {
						this.buildModel();
						this.lastTimestamp = stamp;
						this.refreshed = true;
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
		} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	}
	
	private void buildModel() throws UnknownHostException {
		// Map of user preferences by Mahout user id
		FastByIDMap<Collection<Preference>> userIDPrefMap = new FastByIDMap<Collection<Preference>>();
		System.out.println("Building model");
		
		try {
			this.transport = new THttpClient(this.datahubHost);
			this.protocol = new  TBinaryProtocol(transport);
			this.client = new DataHub.Client(protocol);
			System.out.println("Connected to Datahub Successfully");
		
			this.con_params = new ConnectionParams();
			this.con_params.setUser(this.datahubUsername);
			this.con_params.setPassword(this.datahubPassword);
			this.conn = this.client.open_connection(con_params);
			
			if (this.datahubTableName != null) {
				int numItems = this.getItemCount(this.datahubTableName);
				System.out.println(this.datahubTableName);
				System.out.println(numItems);
				for (int i = 0; i < numItems; i += 10000) {
					ResultSet res;
					Thread.sleep(4000);
					synchronized(this.client) {
						res = this.client.execute_sql(this.conn, "select * from " + this.datahubDatabase + "." + this.datahubTableName + " limit " + 10000 + " offset " + i, null);
					}
					HashMap<String, Integer> colToIndex = this.getFieldNames(res);
					
					if (res != null) {
						for (Tuple t : res.getTuples()) {
							List<ByteBuffer> cells = t.getCells();
							long userID = Long.parseLong(new String(cells.get(colToIndex.get("user_id")).array()));
							long itemID = Long.parseLong(new String(cells.get(colToIndex.get("item_id")).array()));
							long ratingValue = Long.parseLong(new String(cells.get(colToIndex.get("rating")).array()));

							Collection<Preference> userPrefs = userIDPrefMap.get(userID);
							if (userPrefs == null) {
								userPrefs = Lists.newArrayListWithCapacity(2);
								userIDPrefMap.put(userID, userPrefs);
							}
							userPrefs.add(new GenericPreference(userID, itemID, ratingValue));
						}
					}
				}
			}
			
			synchronized(this.client) {
				ResultSet last = this.client.execute_sql(this.conn, "SELECT MAX(updated) FROM " + this.datahubDatabase + "." + "update_log where table_name='" + this.datahubTableName + "'", null);
				
				if(last != null) {
					for (Tuple t : last.getTuples()) {
						List<ByteBuffer> cells = t.getCells();
						this.lastTimestamp = new String(cells.get(0).array());
					}
				}
				
				this.generalModel = new GenericDataModel(GenericDataModel.toDataMap(userIDPrefMap, true));
				this.delegate = new PlusAnonymousConcurrentUserDataModel(this.generalModel, 10);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets list of all items from items table
	 */
	public List<Item> getItems(String table) {
		List<Item> items = new ArrayList<Item>();
		try {
			ResultSet res;
			synchronized(this.client) {
				res = this.client.execute_sql(this.conn, "select * from " + this.datahubDatabase + "." + table, null);
			}
			
			HashMap<String, Integer> colToIndex = this.getFieldNames(res);
			
			for (Tuple t : res.getTuples()) {
				List<ByteBuffer> cells = t.getCells();
				Item item = new Item();
				item.setId(Long.parseLong(new String(cells.get(colToIndex.get("id")).array())));
				item.setTitle(new String(cells.get(colToIndex.get("title")).array()));
				item.setDescription(new String(cells.get(colToIndex.get("description")).array()));
				item.setImage(new String(cells.get(colToIndex.get("image")).array()));
				items.add(item);
			}
			return items;
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
	 * Display a page of items
	 */
	public List<Item> getPageItems(String table, long page, long numPerPage) {
		List<Item> items = new ArrayList<Item>();
		try {
			ResultSet res;
			synchronized(this.client) {
				res = this.client.execute_sql(this.conn, "select * from " + this.datahubDatabase + "." + table + " limit " + numPerPage + " offset " + numPerPage * page, null);
			}
			HashMap<String, Integer> colToIndex = this.getFieldNames(res);
			
			for (Tuple t : res.getTuples()) {
				List<ByteBuffer> cells = t.getCells();
				Item item = new Item();
				item.setId(Long.parseLong(new String(cells.get(colToIndex.get("id")).array())));
				item.setTitle(new String(cells.get(colToIndex.get("title")).array()));
				item.setDescription(new String(cells.get(colToIndex.get("description")).array()));
				item.setImage(new String(cells.get(colToIndex.get("image")).array()));
				items.add(item);
			}
			return items;
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
			ResultSet res;
			synchronized(this.client) {
				res = this.client.execute_sql(this.conn, "select count(*) from " + this.datahubDatabase + "." + table, null);
			}
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
	public boolean createNewRecommender(String table) {
		try {
			synchronized(this.client) {
				this.client.execute_sql(this.conn, "alter table " + this.datahubDatabase + "." + table + " add id serial", null);
				this.client.execute_sql(this.conn, "create table " + this.datahubDatabase + "." + table + "_ratings (" + 
					"user_id int, item_id int, rating varchar(255))" , null);
				this.client.execute_sql(this.conn, "create table " + this.datahubDatabase + "." + table + "_users (" + 
					"id SERIAL PRIMARY KEY, username varchar(255), email varchar(255), password varchar(255))" , null);
				this.client.execute_sql(this.conn, "CREATE TABLE " + this.datahubDatabase + ".update_log(table_name text, updated timestamp NOT NULL DEFAULT now());" 
										+ "CREATE FUNCTION timestamp_update_log() RETURNS TRIGGER LANGUAGE 'plpgsql' AS $$" 
									    + "BEGIN INSERT INTO " + this.datahubDatabase + ".update_log(table_name) VALUES(TG_TABLE_NAME); RETURN NEW;"
									    + "END $$", null);
				this.client.execute_sql(this.conn, "CREATE TRIGGER " + table + "_timestamp_update_log"
												+ "AFTER INSERT OR UPDATE ON " + this.datahubDatabase + "." + table + " FOR EACH STATEMENT EXECUTE procedure timestamp_update_log();", null);
				this.client.execute_sql(this.conn, "CREATE TRIGGER " + table + "_ratings_timestamp_update_log"
												+ "AFTER INSERT OR UPDATE ON " + this.datahubDatabase + "." + table + "_ratings FOR EACH STATEMENT EXECUTE procedure timestamp_update_log();", null);
			}
			return true;
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Gets list of items user has rated.
	 */
	 public List<Item> getUserRatedItems(long userId, String ratings_table, String items_table) {
		List<Item> items = new ArrayList<Item>();
		try {
			ResultSet res;
			synchronized(this.client) {
				res = this.client.execute_sql(this.conn, "SELECT item_id, rating FROM " + ratings_table + " WHERE user_id=" + userId, null);
			}
			HashMap<String, Integer> colToIndex = this.getFieldNames(res);

			for (Tuple t : res.getTuples()) {
				List<ByteBuffer> cells = t.getCells();
				Item item = new Item();
				ResultSet s;
				synchronized(this.client) {
					s = this.client.execute_sql(this.conn, "SELECT * FROM " + items_table + " WHERE id='" + new String(cells.get(colToIndex.get("item_id")).array()) + "'", null);
				}
				HashMap<String, Integer> itemColsToIndex = this.getFieldNames(s);
				for (Tuple tt : s.getTuples()) {
					List<ByteBuffer> cc = tt.getCells();
					item.setId(Long.parseLong(new String(cc.get(itemColsToIndex.get("id")).array())));
					item.setTitle(new String(cc.get(itemColsToIndex.get("title")).array()));
					item.setDescription(new String(cc.get(itemColsToIndex.get("description")).array()));
					item.setImage(new String(cc.get(itemColsToIndex.get("image")).array()));
					item.setRating(Long.parseLong(new String(cells.get(colToIndex.get("rating")).array())));
					items.add(item);
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
		if (!saveIntoDb(columns, null , columnNames, ratings_table)) {
			deleteRatings(userId, itemId, ratings_table);
			saveIntoDb(columns, null, columnNames, ratings_table);
		}
		/*try {
			this.delegate.setPreference(userId, itemId, (float) rating);
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		this.delegate.refresh(null);
	 }
	 
	 /**
	  * Gets user ID
	  */
	 public long retrieveUserId(String username, String password, String table) {
		 try {
			 ResultSet res;
			 synchronized(this.client) {
				 res = this.client.execute_sql(this.conn, "SELECT id,password FROM " + table + " WHERE username = '" + username + "'", null);
			 }
			 HashMap<String, Integer> colToIndex = this.getFieldNames(res);

			 for (Tuple t : res.getTuples()) {
				List<ByteBuffer> cells = t.getCells();
				long id = Long.parseLong(new String(cells.get(colToIndex.get("id")).array()));
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
		 this.delegate.refresh(null);
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
				Connection connection = client.open_connection(con_params);
				
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
					Connection connection = client.open_connection(con_params);
					
					ResultSet res;
					synchronized(this.client) {
						res = this.client.execute_sql(connection, "SELECT COUNT(*) FROM " + DatahubDataModel.getDefaultDatahubTablename() + " WHERE username='" + username + "'", null);
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
	 * Gets item from item id
	 */
	public Item getItemFromId(long id, String table) {
		try {
			ResultSet res;
			
			synchronized(this.client) {
				res = this.client.execute_sql(this.conn, "SELECT * FROM " + table + " WHERE id='" + id + "'", null);
			}
			HashMap<String, Integer> colToIndex = this.getFieldNames(res);
			
			for (Tuple t : res.getTuples()) {
				List<ByteBuffer> cells = t.getCells();
				Item item = new Item();
				item.setId(Long.parseLong(new String(cells.get(colToIndex.get("id")).array())));
				item.setTitle(new String(cells.get(colToIndex.get("title")).array()));
				item.setDescription(new String(cells.get(colToIndex.get("description")).array()));
				item.setImage(new String(cells.get(colToIndex.get("image")).array()));
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
	public List<Item> getSearchItems(String table, String query) {
		List<Item> items = new ArrayList<Item>();
		
		String q = "select * from " + table + " where title like ";
		String[] words = query.split(" ");
		for (int i = 0; i < words.length - 1; i++) {
			q += "'%" + words[i] + "%' AND title like ";
		}
		q += "'%" + words[words.length -1] + "%'";
		try {
			ResultSet res;
			synchronized(this.client) {
				res = this.client.execute_sql(this.conn, q, null);
			}
			HashMap<String, Integer> colToIndex = this.getFieldNames(res);
			
			for (Tuple t : res.getTuples()) {
				List<ByteBuffer> cells = t.getCells();
				Item item = new Item();
				item.setId(Long.parseLong(new String(cells.get(colToIndex.get("id")).array())));
				item.setTitle(new String(cells.get(colToIndex.get("title")).array()));
				item.setDescription(new String(cells.get(colToIndex.get("description")).array()));
				item.setImage(new String(cells.get(colToIndex.get("image")).array()));
				items.add(item);
			}
			return items;
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
	 * Removes Datahub User Item
	 */
	private void removeDatahubUserItem(String userID, String itemID) {
	}
	
	/**
	 * Adds Datahub User Item
	 */
	private void addDatahubUserItem(String userID, String itemID) {
	}
	
	/*private DataModel removeUserItem(long userID, Iterable<List<String>> items) {
		FastByIDMap<PreferenceArray> rawData = ((GenericDataModel) delegate).getRawUserData();
		return new GenericDataModel(rawData);
	}*/
	
	private HashMap<String, Integer> getFieldNames(ResultSet res) {
		List<String> fieldNames = res.getField_names();
		HashMap<String, Integer> colToIndex = new HashMap<String, Integer>();
		for (int i = 0; i < fieldNames.size(); i++) {
			colToIndex.put(fieldNames.get(i), i);
	    }
		return colToIndex;
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
}
