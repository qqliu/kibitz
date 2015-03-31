package kibitz;

import java.io.File;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
//import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.file.FileItemSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;

import updates.UpdateLocalFiles;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class IndividualRecommender {
	private DatahubDataModel dataModel;
	private MysqlDataSource dataSource;
	private UserSimilarity userSimilarity;
	private UserNeighborhood neighborhood;
	private GenericUserBasedRecommender recommender;
	private GenericItemBasedRecommender itemRecommender;
	private FileItemSimilarity itemUserSimilarity;
	//private CachingRecommender cachingRecommender;
	
	private String items_table;
	private String username;
	private String password;
	private String ratings_table;
	private String users_table;
	private String databaseName;
	
	public IndividualRecommender(MysqlDataSource dataSource) {
		this.dataModel = null;
		this.dataSource = dataSource;
		this.username = "quanquan";
		this.password = "hof9924ne@!";
		this.ratings_table = "data";
		this.items_table = "data";
		this.databaseName = this.dataSource.getDatabaseName();
	}
	
	public List<Item> makeRecommendation(long userId, long numRecs, List<String> displayColumns) {
		try {
			if (this.dataModel != null) {
				List<Boolean> recommenderTypes = this.dataModel.getRecommenderTypes();
				
				long startTime = System.nanoTime();
				List<RecommendedItem> recommendations = this.recommender.recommend((int) userId, (int) numRecs);
				if (recommendations.size() == 0 && recommenderTypes.get(0)) {
					recommendations = this.itemRecommender.recommend((int) userId, (int) numRecs);
				}
				
				ArrayList<Long> recommendationNames = new ArrayList<Long>();
				for (int i = 0; i < recommendations.size(); i++) {
					recommendationNames.add(recommendations.get(i).getItemID());
				}
				List<Item> recs = this.dataModel.getItemsFromIds(recommendationNames, this.databaseName + "." + this.items_table, 
						this.databaseName + "." + this.ratings_table, userId, displayColumns);
				long endTime = System.nanoTime();
				System.out.println("Time it takes to get recommendation: " + (endTime - startTime));
				
				if (recs.size() == 0 && recommenderTypes.get(2)) {
					recs = this.dataModel.makeOverallRatingsBasedRecommendation(this.databaseName + "." + this.items_table, numRecs, displayColumns);
				}
				
				if (recs.size() == 0 && recommenderTypes.get(1)) {
					return this.dataModel.makeRandomRecommmendation(numRecs, this.databaseName + "." + this.items_table, displayColumns);
				} else {
					return recs;
				}
			}
		} catch (TasteException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/*public List<Item> getItems() {
		List<Item> results = this.dataModel.getItems(this.items_table);
		return results;
	}*/
	
	public List<Item> getSearchItems(String query, List<String> displayColumns) {
		List<Item> results = this.dataModel.getSearchItems(this.databaseName + "." + this.items_table, query, displayColumns);
		return results;
	}
	
	public List<Item> getPageItems(long page, long numPerPage, List<String> displayColumns) {
		List<Item> results = this.dataModel.getPageItems(this.items_table, page, numPerPage, displayColumns);
		return results;
	}
	
	public int getItemCount() {
		return this.dataModel.getItemCount(this.items_table);
	}
	
	public void recordRatings(long userId, long itemId, long rating) {
		this.dataModel.recordRatings(userId, itemId, rating, this.databaseName + "." + this.ratings_table);
	}
	
	public void deleteRatings(long userId, long itemId) {
		this.dataModel.deleteRatings(userId, itemId, this.databaseName + "." + this.ratings_table);
	}
	
	public long retrieveUserId(String username, String password) {
		return this.dataModel.retrieveUserId(username, password, this.databaseName + "." + this.users_table);
	}
	
	public List<Item> makeItemBasedRecommendations(long userId, long numRecs, List<String> displayColumns) {
		long[] itemIds = this.dataModel.getUserRatedItemsIds(userId, this.databaseName + "." + this.ratings_table);
		try {
			List<RecommendedItem> recs = this.itemRecommender.mostSimilarItems(itemIds, (int) numRecs);
			ArrayList<Long> recommendationNames = new ArrayList<Long>();
			for (int i = 0; i < recs.size(); i++) {
				recommendationNames.add(recs.get(i).getItemID());
			}
			List<Item> recommendations = this.dataModel.getItemsFromIds(recommendationNames, this.databaseName + "." + this.items_table, 
					this.databaseName + "." + this.ratings_table, userId, displayColumns);
			return recommendations;
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<Item>();
	}
	
	public List<Item> makeOverallRatingBasedRecommendation(String ratingColumnName, long numRecs, List<String> displayColumns) {
		return this.dataModel.makeOverallRatingsBasedRecommendation(this.databaseName + "." + this.items_table, numRecs, displayColumns);
	}
	
	public List<Item> makeRandomRecommendation(long numRecs, List<String> displayColumns) {
		return this.dataModel.makeRandomRecommmendation(numRecs, this.databaseName + "." + this.items_table, displayColumns);
	}
 	
	public String createNewUser(String username, String email, String password, boolean isKibitzUser) {
		if (this.checkUsername(username, isKibitzUser)) {
			return "User already exists, please pick another username.";
		}
		List<String> columns = new ArrayList<String>();
		columns.add("'" + username + "'");
		columns.add("'" + email + "'");
		List<String> columnNames = new ArrayList<String>();
		columnNames.add("username");
		columnNames.add("email");
		columnNames.add("password");
		String passwordHash;
		try {
			passwordHash = generatePasswordHash(password);
			columns.add("'" + passwordHash + "'");
			if (isKibitzUser) {
				this.dataModel.createNewUser(columns, null, columnNames, null, true);
			} else {
				this.dataModel.createNewUser(columns, null, columnNames, this.databaseName + "." + this.users_table, false);
			}
			return "New user created";
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return "Cannot create user";
	}
	
	public boolean checkUsername(String username, boolean isKibitzUser) {
		if (isKibitzUser) {
			return this.dataModel.checkUsername(username, null, true);
		} else {
			return this.dataModel.checkUsername(username, this.databaseName + "." + this.users_table, false);
		}
	}
	
	public boolean checkLogin(String username, String password, boolean isKibitzUser) {
		String hash;
		if (isKibitzUser) {
			hash = this.dataModel.checkLogin(username, password, null, true); 
		} else {
			hash = this.dataModel.checkLogin(username, password, this.databaseName + "." + this.users_table, false); 
		}
		try {
			if (hash != null) {
				return validatePassword(password, hash);
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
    public static String generatePasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt().getBytes();
         
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }
     
    private static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt.toString();
    }
     
    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }
    
    public static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);
         
        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();
         
        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }
    
    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
    
    public List<Item> getUserRatedItems(long userId, List<String> displayColumns) {
		List<Item> items = this.dataModel.getUserRatedItems(userId, 
				this.databaseName + "." + this.ratings_table, this.databaseName + "." + this.items_table, displayColumns);
		return items;
    }
	
	public void initiateModel(String table, String username, String password, String database) {
		try {
			if (table != null) {
				this.items_table = table;
			}
			
			if (username != null && password != null) {
				this.username = username;
				this.password = password;
			}
			
			if (database != null) {
				this.databaseName = database;
			}
			
			this.ratings_table = table + "_ratings";
			this.users_table = table + "_users";
			
			
			this.dataModel = new DatahubDataModel(this.dataSource.getServerName(), this.databaseName, this.username,
						this.password,
						this.ratings_table);
			
			List<Boolean> recs = this.dataModel.getRecommenderTypes();
			
			if (KibitzServer.RECOMMENDERS.get(table + username + password + database) != null) {
				this.recommender = (GenericUserBasedRecommender) KibitzServer.RECOMMENDERS.get(table + username + password + database);
			} else {
				this.userSimilarity = new PearsonCorrelationSimilarity(this.dataModel);
				this.neighborhood =
					      new NearestNUserNeighborhood(30, userSimilarity, this.dataModel);
				this.recommender = new GenericUserBasedRecommender(this.dataModel, neighborhood, userSimilarity);
				//this.cachingRecommender = new CachingRecommender(recommender);
				KibitzServer.RECOMMENDERS.put(table + username + password + database, this.recommender);
			}
			
			if (recs != null) {
				if (recs.get(0)) {
					if (KibitzServer.RECOMMENDERS.get(table + username + password + database + "items") != null) {
						this.itemRecommender = (GenericItemBasedRecommender) KibitzServer.RECOMMENDERS.get(table + username + password + database + "items");
					} else {
						this.itemUserSimilarity = new FileItemSimilarity(new File(UpdateLocalFiles.getKibitzLocalStorageAddr() + username + 
						"/" + database + "/" + table + "_item_similarity.csv"));
						this.itemRecommender = new GenericItemBasedRecommender(this.dataModel, this.itemUserSimilarity);
						KibitzServer.RECOMMENDERS.put(table + username + password + database + "items", this.itemRecommender);
					}
				}
			}
		} catch (UnknownHostException e) {
			System.err.println(e);
		} catch (TasteException e) {
			System.err.println(e);
		}
	}
	
	public List<Item> getItemsFromPrimaryKeys(String primaryKey, List<String> itemKeys, List<String> displayColumns) {
		return this.dataModel.getItemsFromPrimaryKeys(primaryKey, itemKeys, displayColumns, this.databaseName + "." + this.items_table);
	}
	
	public static IndividualRecommender createNewIndividualServer(MysqlDataSource dataSource) {
		return new IndividualRecommender(dataSource);
	}
	
	public void updateDataModel() {
		this.dataModel.refresh(null);
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getTable() {
		return this.items_table;
	}
	
	public String getDatabase() {
		return this.databaseName;
	}
	
	public boolean getRefreshed() {
		return this.dataModel.getRefreshed();
	}
	
	public String getTimestamp() {
		return this.dataModel.getTimestamp();
	}
}
