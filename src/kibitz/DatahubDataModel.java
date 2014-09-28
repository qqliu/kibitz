package kibitz;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

import javax.sql.DataSource;

import com.google.common.collect.Lists;

public class DatahubDataModel implements DataModel{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(DatahubDataModel.class);

	/** Default Datahub host. */
	private static final String DEFAULT_DATAHUB_HOST = "sql.mit.edu/";

	/** Default Datahub user. */
	private static final String DEFAULT_DATAHUB_USERNAME = "quanquan";

	/**Default Datahub password. */
	private static final String DEFAULT_DATAHUB_PASSWORD = "XXXXXXXXXXX";

	/** Default Datahub Database */
	private static final String DEFAULT_DATAHUB_DATABASE = "quanquan+grouplens";

	/** Default Datahub Table Name*/
	private static final String DEFAULT_DATAHUB_TABLENAME = "ratings";
	private final String url = "jdbc:mysql://";

	private String datahubHost = DEFAULT_DATAHUB_HOST;
	private String datahubUsername = DEFAULT_DATAHUB_USERNAME;
	private String datahubPassword = DEFAULT_DATAHUB_PASSWORD;
	private String datahubDatabase = DEFAULT_DATAHUB_DATABASE;
	private String datahubTableName = DEFAULT_DATAHUB_TABLENAME;

	private final ReentrantLock reloadLock;

	private GenericDataModel delegate;

	/**
	* Creates a new DatahubDataModel
	*/
	public DatahubDataModel() throws UnknownHostException {
		this.reloadLock = new ReentrantLock();
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
		this.reloadLock = new ReentrantLock();
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
		Connection conn = null;
		System.out.println("Building model");

		try {
		    Class.forName("com.mysql.jdbc.Driver").newInstance();
		    System.out.println(this.datahubHost);
		    System.out.println(this.datahubUsername);
		    System.out.println(this.datahubPassword);
		    conn = DriverManager.getConnection (this.url + this.datahubHost + this.datahubDatabase, this.datahubUsername, this.datahubPassword);
		    System.out.println ("Database connection established");

		    Statement s = conn.createStatement();
		    // execute the query, and get a java resultset
		    ResultSet rs = s.executeQuery("SELECT * FROM ratings");
		    while (rs.next()) {
		        int userID = rs.getInt("id");
		        int itemID = rs.getInt("itemID");
		        int ratingValue = rs.getInt("rating");
		        System.out.println(userID + ", " + itemID + ", " + ratingValue);

		        Collection<Preference> userPrefs = userIDPrefMap.get(userID);
				if (userPrefs == null) {
					userPrefs = Lists.newArrayListWithCapacity(2);
		            userIDPrefMap.put(userID, userPrefs);
				}
				userPrefs.add(new GenericPreference(userID, itemID, ratingValue));
		    }
		    s.close();
		}
		catch (Exception e) {
		    System.err.println ("Database server error: " + e);
		} finally {
			if (conn != null) {
		        try {
		            conn.close ();
		            System.out.println ("Database connection terminated");
		        } catch (Exception e) { /* ignore close errors */ }
		    }
		}

		this.delegate = new GenericDataModel(GenericDataModel.toDataMap(userIDPrefMap, true));
	}

	/**
	 * Maps id from Datahub to long for Mahout recommenders
	 */
	public String fromIdToLong(String id, boolean isUser) {
		// creates a map of the id to the long value
		return id;
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

	/*@Override
	public DataSource getDataSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FastByIDMap<PreferenceArray> exportWithPrefs() throws TasteException {
		// TODO Auto-generated method stub
		return delegate.getRawItemData();
	}

	@Override
	public FastByIDMap<FastIDSet> exportWithIDsOnly() throws TasteException {
		// TODO Auto-generated method stub
		return null;
	}*/
}
