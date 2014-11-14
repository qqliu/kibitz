package kibitz;

import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
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
	
	private TTransport transport;
	private TProtocol protocol;
	private DataHub.Client client;
	private ConnectionParams con_params;
	private Connection conn;

	private GenericDataModel delegate;

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
				ResultSet res = this.client.execute_sql(this.conn, "select * from " + this.datahubDatabase + "." + this.datahubTableName, null);
		
				if (res != null) {
					for (Tuple t : res.getTuples()) {
						List<ByteBuffer> cells = t.getCells();
						long userID = Long.parseLong(new String(cells.get(0).array()));
						long itemID = Long.parseLong(new String(cells.get(1).array()));
						int ratingValue = Integer.parseInt(new String(cells.get(2).array()));

						Collection<Preference> userPrefs = userIDPrefMap.get(userID);
						if (userPrefs == null) {
							userPrefs = Lists.newArrayListWithCapacity(2);
							userIDPrefMap.put(userID, userPrefs);
						}
						userPrefs.add(new GenericPreference(userID, itemID, ratingValue));
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		this.delegate = new GenericDataModel(GenericDataModel.toDataMap(userIDPrefMap, true));
	}
	
	/**
	 * Gets list of all items from items table
	 */
	public List<Item> getItems(String table) {
		List<Item> items = new ArrayList<Item>();
		try {
			ResultSet res = this.client.execute_sql(this.conn, "select * from " + this.datahubDatabase + "." + table, null);
			for (Tuple t : res.getTuples()) {
				List<ByteBuffer> cells = t.getCells();
				Item item = new Item();
				item.setId(Long.parseLong(new String(cells.get(3).array())));
				item.setTitle(new String(cells.get(0).array()));
				item.setDescription(new String(cells.get(1).array()));
				item.setImage(new String(cells.get(2).array()));
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
	 * Creates all associated tables associated with a new recommender.
	 */
	public boolean createNewRecommender(String table) {
		try {
			this.client.execute_sql(this.conn, "alter table " + this.datahubDatabase + "." + table + " add id serial", null);
			this.client.execute_sql(this.conn, "create table " + this.datahubDatabase + "." + table + "_ratings (" + 
					"user_id int, item_id int, rating varchar(255))" , null);
			this.client.execute_sql(this.conn, "create table " + this.datahubDatabase + "." + table + "_users (" + 
					"id SERIAL PRIMARY KEY, username varchar(255), email varchar(255), password varchar(255))" , null);
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
	 public List<Item> getUserRatedItems(int userId, String ratings_table, String items_table) {
		List<Item> items = new ArrayList<Item>();
		try {
			ResultSet res = this.client.execute_sql(this.conn, "SELECT item_id, rating FROM " + ratings_table + " WHERE user_id=" + userId, null);
			for (Tuple t : res.getTuples()) {
				List<ByteBuffer> cells = t.getCells();
				Item item = new Item();
				ResultSet s = this.client.execute_sql(this.conn, "SELECT * FROM " + items_table + " WHERE id='" + new String(cells.get(0).array()) + "'", null);
				for (Tuple tt : s.getTuples()) {
					List<ByteBuffer> cc = tt.getCells();
					item.setId(Long.parseLong(new String(cc.get(3).array())));
					item.setTitle(new String(cc.get(0).array()));
					item.setDescription(new String(cc.get(1).array()));
					item.setImage(new String(cc.get(2).array()));
					item.setRating(Integer.parseInt(new String(cells.get(1).array())));
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
	 public void recordRatings(int userId, int itemId, int rating, String ratings_table) {
		List<Integer> columns = new ArrayList<Integer>();
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
	 }
	 
	 /**
	  * Gets user ID
	  */
	 public long retrieveUserId(String username, String password, String table) {
		 try {
			 ResultSet res = this.client.execute_sql(this.conn, "SELECT id,password FROM " + table + " WHERE username = '" + username + "'", null);
			 for (Tuple t : res.getTuples()) {
				List<ByteBuffer> cells = t.getCells();
				long id = Long.parseLong(new String((cells.get(0).array())));
				String storedPassword = new String(cells.get(1).array());
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
	 public void deleteRatings(int userId, int itemId, String table) {
		 System.out.println("deleting");
		 try {
			 	this.client.execute_sql(this.conn, "DELETE FROM " + table + " WHERE user_id = " + userId + " AND "
			 			+ "item_id = " + itemId, null);
		 } catch (DBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		 } catch (TException e) {
			 	// TODO Auto-generated catch block
			 	e.printStackTrace();
		 }
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
							System.out.println(columns.get(i).getClass().getName());
							q = q + columns.get(i) + " AND ";
						} else {
							q = q + columns.get(i) + " AND ";
						}
					}
				}
				q = q.substring(0, q.length() - 5);
				System.out.println("checking query if rating exists");
				System.out.println(q);
				ResultSet res = this.client.execute_sql(this.conn, q, null);
				for (Tuple t : res.getTuples()) {
					List<ByteBuffer> cells = t.getCells();
					int num = Integer.parseInt(new String((cells.get(0).array())));
					if (num == 0) {
						this.client.execute_sql(this.conn, "INSERT INTO " + table + "(" + StringUtils.join(columnNames, ",") + ") VALUES (" + StringUtils.join(columns, ",") + ");", null);
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
				System.out.println("Connected to central Datahub repo successfully");
			
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
				ResultSet res = this.client.execute_sql(this.conn, "SELECT password FROM " + table + " WHERE username='" + username + "'", null);
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
					System.out.println("Connected to central Datahub repo successfully");
				
					ConnectionParams params = new ConnectionParams();
					params.setUser(DatahubDataModel.getDefaultDatahubUsername());
					params.setPassword(DatahubDataModel.getDefaultDatahubPassword());
					Connection connection = client.open_connection(con_params);
					
					ResultSet res = this.client.execute_sql(connection, "SELECT COUNT(*) FROM " + DatahubDataModel.getDefaultDatahubTablename() + " WHERE username='" + username + "'", null);
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
					ResultSet res = this.client.execute_sql(this.conn, "SELECT COUNT(*) FROM " + table + " WHERE username='" + username + "'", null);
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
			ResultSet res = this.client.execute_sql(this.conn, "SELECT * FROM " + table + " WHERE id='" + id + "'", null);
			for (Tuple t : res.getTuples()) {
				List<ByteBuffer> cells = t.getCells();
				Item item = new Item();
				item.setId(Long.parseLong(new String(cells.get(3).array())));
				item.setTitle(new String(cells.get(0).array()));
				item.setDescription(new String(cells.get(1).array()));
				item.setImage(new String(cells.get(2).array()));
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
	 * Maps Mahout long value to Datahub ID
	 */
	public String fromLongToId(long id) {
		return new String();
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
	
	private DataModel removeUserItem(long userID, Iterable<List<String>> items) {
		FastByIDMap<PreferenceArray> rawData = ((GenericDataModel) delegate).getRawUserData();
		return new GenericDataModel(rawData);
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
