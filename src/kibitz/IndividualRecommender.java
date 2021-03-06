package kibitz;

import java.io.File;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
//import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.file.FileItemSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;

import updates.UpdateLocalFiles;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * IndividualRecommender: Mini-server for managing
 * each individual recommender server. Responsible for
 * generating recommendations for each site.
 *
 * @author Quanquan Liu
 *
 * @date 10/22/2014
*/

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
	private String ratings_table;
	private String users_table;
	private String databaseName;

	public IndividualRecommender(MysqlDataSource dataSource) {
		this.dataModel = null;
		this.dataSource = dataSource;
		this.username = "quanquan";
		this.ratings_table = "data";
		this.items_table = "data";
		this.databaseName = this.dataSource.getDatabaseName();
	}

	public List<Item> makeRecommendation(long userId, long numRecs, boolean isBoolean, List<String> displayColumns) {
		try {
			if (this.dataModel != null) {
				List<Boolean> recommenderTypes = this.dataModel.getRecommenderTypes();
				long startTime = System.nanoTime();
				List<RecommendedItem> recommendations;

				recommendations = this.recommender.recommend((int) userId, (int) numRecs);
				System.out.println(this.dataModel.getPrefFileLocation());

				UncenteredCosineSimilarity cosSim = new UncenteredCosineSimilarity(this.dataModel);
				NearestNUserNeighborhood neighborhood = new NearestNUserNeighborhood(30, cosSim, this.dataModel);
				GenericUserBasedRecommender cosRec = new GenericUserBasedRecommender(this.dataModel, neighborhood, cosSim);
				List<RecommendedItem> cosineRecs = cosRec.recommend((int) userId, (int) numRecs);

				EuclideanDistanceSimilarity euSim = new EuclideanDistanceSimilarity(this.dataModel);
				neighborhood = new NearestNUserNeighborhood(30, euSim, this.dataModel);
				GenericUserBasedRecommender euRec = new GenericUserBasedRecommender(this.dataModel, neighborhood, euSim);
				List<RecommendedItem> euRecs = euRec.recommend((int) userId, (int) numRecs);

				System.out.println(recommendations);
				System.out.println(cosineRecs);
				System.out.println(euRecs);

				HashMap<Long, Integer> numberOccurrences = new HashMap<Long, Integer>();
				for (int i = 0; i < recommendations.size(); i++) {
					long itemId = recommendations.get(i).getItemID();
					numberOccurrences.put(itemId, 1);
				}

				for (int i = 0; i < cosineRecs.size(); i++) {
					long itemId = cosineRecs.get(i).getItemID();
					if (numberOccurrences.containsKey(itemId)) {
						int occurrences = numberOccurrences.get(itemId);
						numberOccurrences.put(itemId, occurrences + 1);
					} else {
						numberOccurrences.put(itemId, 1);
					}
				}

				for (int i = 0; i < euRecs.size(); i++) {
					long itemId = euRecs.get(i).getItemID();
					if (numberOccurrences.containsKey(itemId)) {
						int occurrences = numberOccurrences.get(itemId);
						numberOccurrences.put(itemId, occurrences + 1);
					} else {
						numberOccurrences.put(itemId, 1);
					}
				}

                //System.out.println(this.recommender.estimatePreference(5, 98));
                //System.out.println(this.recommender.recommend(5, 10));
				if (numberOccurrences.size() == 0 && recommenderTypes.get(0)) {
					recommendations = this.itemRecommender.recommend((int) userId, (int) numRecs);

					for (int i = 0; i < recommendations.size(); i++) {
						long itemId = recommendations.get(i).getItemID();
						numberOccurrences.put(itemId, 1);
					}
				}

				ArrayList<Long> recommendationNames = new ArrayList<Long>();
				for (Long key: numberOccurrences.keySet()) {
					recommendationNames.add(key);
				}

				List<Item> recs = this.dataModel.getItemsFromIds(recommendationNames, this.databaseName + "." + this.items_table,
						this.databaseName + "." + this.ratings_table, userId, displayColumns);

				if (recs.size() == 0 && recommenderTypes.get(2)) {
					recs = this.dataModel.makeOverallRatingsBasedRecommendation(this.databaseName + "." + this.items_table, this.databaseName + "." + this.ratings_table, numRecs, displayColumns);
				}

				for (int i = 0; i < recs.size(); i++) {
					Item rec = recs.get(i);
					long k = rec.getKibitz_generated_id();
					if (numberOccurrences.containsKey(k)) {
						rec.setConfidence(numberOccurrences.get(k));
						double perPredictedPreference = recommender.estimatePreference(userId, k);
						double cosPredictedPreference = cosRec.estimatePreference(userId, k);
						double euPredictedPreference = euRec.estimatePreference(userId, k);
						double s = 0.0;
						int c = 0;
						if (!Double.isNaN(perPredictedPreference)) {
							s += perPredictedPreference;
							c += 1;
						}

						if (!Double.isNaN(cosPredictedPreference)) {
							s += cosPredictedPreference;
							c += 1;
						}

						if (!Double.isNaN(euPredictedPreference)) {
							s += euPredictedPreference;
							c += 1;
						}

						if (c > 0)
							rec.setPredictedPreferences(s/c);
						else
							rec.setPredictedPreferences(-1);
					} else {
						rec.setConfidence(0);
						rec.setPredictedPreferences(-1);
					}
				}

				long endTime = System.nanoTime();
				System.out.println("Time it takes to get recommendation: " + (endTime - startTime));

				if (recs.size() == 0 && recommenderTypes.get(1)) {
					return this.dataModel.makeRandomRecommmendation(numRecs, this.databaseName + "." + this.items_table, displayColumns);
				} else {
					System.out.println(recs);
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

	public List<Item> getSearchItems(String query, List<String> columnsToSearch, List<String> displayColumns) {
		List<Item> results = this.dataModel.getSearchItems(this.databaseName + "." + this.items_table, query, columnsToSearch, displayColumns);
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

	public long retrieveUserId(String username) {
		return this.dataModel.retrieveUserId(username, this.databaseName + "." + this.users_table);
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
		return this.dataModel.makeOverallRatingsBasedRecommendation(this.databaseName + "." + this.items_table, this.databaseName + "." + this.ratings_table, numRecs, displayColumns);
	}

	public List<Item> makeRandomRecommendation(long numRecs, List<String> displayColumns) {
		return this.dataModel.makeRandomRecommmendation(numRecs, this.databaseName + "." + this.items_table, displayColumns);
	}

	public String createNewUser(String username, boolean isKibitzUser) {
		if (this.checkUsername(username, isKibitzUser)) {
			return "User already exists, please pick another username.";
		}
		List<String> columns = new ArrayList<String>();
		columns.add("'" + username + "'");

		List<String> columnNames = new ArrayList<String>();
		columnNames.add("username");

		boolean created;
		if (isKibitzUser) {
			created = this.dataModel.createNewUser(columns, null, columnNames, null, true);
		} else {
			created = this.dataModel.createNewUser(columns, null, columnNames, this.databaseName + "." + this.users_table, false);
		}

		if (created)
			return "New user created";
		else
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

	public void initiateModel(String key, String table, String username, String database) {
		try {
			if (table != null) {
				this.items_table = table;
			}

			if (username != null) {
				this.username = username;
			}

			if (database != null) {
				this.databaseName = database;
			}

			this.ratings_table = table + "_ratings";
			this.users_table = table + "_users";


			this.dataModel = new DatahubDataModel(this.dataSource.getServerName(), this.databaseName, this.username, this.ratings_table);

			List<Boolean> recs = this.dataModel.getRecommenderTypes();

			if (KibitzServer.RECOMMENDERS.get(key) != null) {
				this.recommender = (GenericUserBasedRecommender) KibitzServer.RECOMMENDERS.get(key);
			} else {
				this.userSimilarity = new PearsonCorrelationSimilarity(this.dataModel);

				this.neighborhood = new NearestNUserNeighborhood(30, this.userSimilarity, this.dataModel);
				this.recommender = new GenericUserBasedRecommender(this.dataModel, this.neighborhood, this.userSimilarity);
				//this.cachingRecommender = new CachingRecommender(recommender);
				KibitzServer.RECOMMENDERS.put(key, this.recommender);
			}

			if (recs != null) {
				if (recs.get(0)) {
					if (KibitzServer.RECOMMENDERS.get(key) != null) {
						this.itemRecommender = (GenericItemBasedRecommender) KibitzServer.RECOMMENDERS.get(key + "items");
					} else {
						this.itemUserSimilarity = new FileItemSimilarity(new File(UpdateLocalFiles.getKibitzLocalStorageAddr() + username +
						"/" + database + "/" + table + "_item_similarity.csv"));
						this.itemRecommender = new GenericItemBasedRecommender(this.dataModel, this.itemUserSimilarity);
						KibitzServer.RECOMMENDERS.put(key + "items", this.itemRecommender);
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

	public void updateDataModel(String key) {
		try {
			this.userSimilarity = new PearsonCorrelationSimilarity(this.dataModel);
			this.neighborhood = new NearestNUserNeighborhood(30, this.userSimilarity, this.dataModel);
			this.recommender = new GenericUserBasedRecommender(this.dataModel, this.neighborhood, this.userSimilarity);
			//this.cachingRecommender = new CachingRecommender(recommender);
			KibitzServer.RECOMMENDERS.put(key, this.recommender);
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public DatahubDataModel getDataModel() {
		return this.dataModel;
	}

	public String getUsername() {
		return this.username;
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
