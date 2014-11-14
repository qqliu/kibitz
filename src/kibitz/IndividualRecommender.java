package kibitz;

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
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class IndividualRecommender {
	private DatahubDataModel dataModel;
	private MysqlDataSource dataSource;
	private UserSimilarity userSimilarity;
	private UserNeighborhood neighborhood;
	private GenericUserBasedRecommender recommender;
	private CachingRecommender cachingRecommender;
	
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
	
	public List<Item> makeRecommendation(int userId, int numRecs) {
		System.out.println("Making recommendation: ");
		try {
			if (this.dataModel != null) {
				List<RecommendedItem> recommendations = this.cachingRecommender.recommend(userId, numRecs);
				ArrayList<Long> recommendationNames = new ArrayList<Long>();
				ArrayList<Item> recs = new ArrayList<Item>();
				for (int i = 0; i < recommendations.size(); i++) {
					recommendationNames.add(recommendations.get(i).getItemID());
					recs.add(this.dataModel.getItemFromId(recommendations.get(i).getItemID(), this.databaseName + "." + this.items_table));
				}
				System.out.println(recommendationNames);
				return recs;
			}
		} catch (TasteException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Item> getItems() {
		List<Item> results = this.dataModel.getItems(this.items_table);
		System.out.println(results);
		return results;
	}
	
	public void recordRatings(int userId, int itemId, int rating) {
		this.dataModel.recordRatings(userId, itemId, rating, this.databaseName + "." + this.ratings_table);
	}
	
	public void deleteRatings(int userId, int itemId) {
		this.dataModel.deleteRatings(userId, itemId, this.databaseName + "." + this.ratings_table);
	}
	
	public long retrieveUserId(String username, String password) {
		return this.dataModel.retrieveUserId(username, password, this.databaseName + "." + this.users_table);
	}
	
	public String createNewUser(String username, String email, String password, boolean isKibitzUser) {
		if (this.checkUsername(username, isKibitzUser)) {
			System.out.println("User exists");
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
	
    private String generatePasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt().getBytes();
         
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }
     
    private String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt.toString();
    }
     
    private String toHex(byte[] array) throws NoSuchAlgorithmException {
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
        System.out.println(diff == 0);
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
    
    public List<Item> getUserRatedItems(int userId) {
		List<Item> items = this.dataModel.getUserRatedItems(userId, this.databaseName + "." + this.ratings_table, this.databaseName + "." + this.items_table);
		System.out.println(items);
		return items;
    }
    
    public boolean createNewRecommender(String username, String password, String database, String table) {
    		try {
				this.dataModel = new DatahubDataModel(this.dataSource.getServerName(), database, 
					username,
					password,
					null);
				return this.dataModel.createNewRecommender(table);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		return false;
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
			
			this.dataModel = new DatahubDataModel(this.dataSource.getServerName(), this.databaseName, 
					this.username,
					this.password,
					this.ratings_table);
			
			if (KibitzServer.RECOMMENDERS.get(table + username + password + database) != null) {
				this.cachingRecommender = KibitzServer.RECOMMENDERS.get(table + username + password + database);
			} else {
				this.userSimilarity = new PearsonCorrelationSimilarity(this.dataModel);
				this.neighborhood =
					      new NearestNUserNeighborhood(30, userSimilarity, this.dataModel);
				this.recommender = new GenericUserBasedRecommender(this.dataModel, neighborhood, userSimilarity);
				this.cachingRecommender = new CachingRecommender(recommender);
				KibitzServer.RECOMMENDERS.put(table + username + password + database, this.cachingRecommender);
			}
		} catch (UnknownHostException e) {
			System.err.println(e);
		} catch (TasteException e) {
			System.err.println(e);
		}
	}
	
	public static IndividualRecommender createNewIndividualServer(MysqlDataSource dataSource) {
		return new IndividualRecommender(dataSource);
	}
}
