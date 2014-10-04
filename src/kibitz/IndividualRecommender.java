package kibitz;

import kibitz.RecommenderService.Iface;

import java.math.BigInteger;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.lang.StringUtils;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class IndividualRecommender implements Iface {
	private DatahubDataModel dataModel;
	private MysqlDataSource dataSource;
	private UserSimilarity userSimilarity;
	private UserNeighborhood neighborhood;
	private GenericUserBasedRecommender recommender;
	private CachingRecommender cachingRecommender;

	private String table;
	private String username;
	private String password;

	public IndividualRecommender(MysqlDataSource dataSource) {
		this.dataModel = null;
		this.dataSource = dataSource;
		this.username = "quanquan";
		this.password = "XXXXXXXX";
	}

	public List<List<String>> makeRecommendation(int userId, int numRecs) {
		System.out.println("Making recommendation: ");
		try {
			if (this.dataModel != null) {
				List<RecommendedItem> recommendations = this.cachingRecommender.recommend(userId, numRecs);
				ArrayList<Long> recommendationNames = new ArrayList<Long>();
				for (int i = 0; i < recommendations.size(); i++) {
					recommendationNames.add(recommendations.get(i).getItemID());
				}
				System.out.println(recommendationNames);
				return new ArrayList<List<String>>();
			}
		} catch (TasteException e) {
			e.printStackTrace();
		/*} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();*/
		}
		System.out.println("Returning null");
		return null;
	}

	public List<List<String>> getItems() {
		List<List<String>> items = new ArrayList<List<String>>();
		try {
			Connection conn = this.dataSource.getConnection();
			PreparedStatement query = conn.prepareStatement("SELECT * FROM " + this.table + ";");
			ResultSet results = query.executeQuery();
			while(results.next()){
				List<String> item = new ArrayList<String>();
				item.add(Integer.toString(results.getInt(1)));
				item.add(results.getString(2));
				item.add(results.getString(3));
				item.add(results.getString(4));
				items.add(item);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return items;
	}

	public void recordRatings(int userId, int itemId, int rating) {
		List<Integer> columns = new ArrayList<Integer>();
		columns.add(userId);
		columns.add(itemId);
		columns.add(rating);
		List<String> columnNames = new ArrayList<String>();
		columnNames.add("user_id");
		columnNames.add("item_id");
		columnNames.add("rating");
		if (!saveIntoDb(columns, "ratings", columnNames)) {
			deleteRatings(userId, itemId);
			saveIntoDb(columns, "ratings", columnNames);
		}
	}

	public void deleteRatings(int userId, int itemId) {
		System.out.println("deleting");
		try {
			Connection conn = this.dataSource.getConnection();
			PreparedStatement query = conn.prepareStatement("DELETE FROM " + this.table + "ratings WHERE user_id = " + userId + " AND "
					+ "item_id = " + itemId);
			query.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String createNewUser(String username, String email, String password) {
		if (this.checkUsername(username)) {
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
			Connection conn = this.dataSource.getConnection();
			PreparedStatement query = conn.prepareStatement("SELECT id FROM " + this.table + "users WHERE username='" + username + "'");
			ResultSet results = query.executeQuery();
			results.first();
			Integer userid = results.getInt(1);
			return userid.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Cannot create user";
	}

	public boolean checkUsername(String username) {
		try {
			Connection conn = this.dataSource.getConnection();
			PreparedStatement query = conn.prepareStatement("SELECT COUNT(*) FROM " + this.table + "users WHERE username='" + username + "'");
			ResultSet results = query.executeQuery();
			while (results.next()) {
				int num = results.getInt(1);
				System.out.println("user already exists?");
				System.out.println(num);
				if (num == 0) {
					return false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public boolean checkLogin(String username, String password) {
		Connection conn;
		try {
			conn = this.dataSource.getConnection();
			PreparedStatement query = conn.prepareStatement("SELECT password FROM " + this.table + "users WHERE username='" + username + "'");
			ResultSet results = query.executeQuery();
			results.first();
			String hash = results.getString(1);
			return validatePassword(password, hash);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

    private boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
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

    private byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public List<List<String>> getUserRatedItems(int userId) {
		List<List<String>> items = new ArrayList<List<String>>();
		try {
			Connection conn = this.dataSource.getConnection();
			System.out.println("SELECT item_id, rating FROM " + this.table + "ratings WHERE user_id=" + userId);
			PreparedStatement query = conn.prepareStatement("SELECT item_id, rating FROM " + this.table + "ratings WHERE user_id=" + userId);
			ResultSet results = query.executeQuery();
			while(results.next()){
				List<String> item = new ArrayList<String>();
				query = conn.prepareStatement("SELECT * FROM " + this.table + " WHERE id='" + results.getInt(1) + "'");
				ResultSet result = query.executeQuery();
				result.next();
				item.add(Integer.toString(result.getInt(1)));
				item.add(result.getString(2));
				item.add(result.getString(3));
				item.add(result.getString(4));
				System.out.println(Integer.toString(results.getInt(2)));
				item.add(Integer.toString(results.getInt(2)));
				items.add(item);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return items;
    }

	public void initiateModel(String table) {
		try {
			this.dataModel = new DatahubDataModel(this.dataSource.getServerName(), this.dataSource.getDatabaseName(),
					this.username,
					this.password,
					this.dataSource.getDatabaseName());
			this.table = table;
			this.userSimilarity = new PearsonCorrelationSimilarity(this.dataModel);
			this.neighborhood =
				      new NearestNUserNeighborhood(30, userSimilarity, this.dataModel);
			this.recommender = new GenericUserBasedRecommender(this.dataModel, neighborhood, userSimilarity);
			this.cachingRecommender = new CachingRecommender(recommender);
		} catch (UnknownHostException e) {
			System.err.println(e);
		} catch (TasteException e) {
			System.err.println(e);
		}
	}

	private boolean saveIntoDb(List<?> columns, String suffix, List<String>columnNames) {
		try {
			Connection conn = this.dataSource.getConnection();
			String q = "SELECT COUNT(*) FROM " + this.table + suffix + " WHERE ";
			for (int i = 0; i < columns.size(); i++) {
				if (columnNames.get(i) != "rating") {
					q = q + columnNames.get(i) + "=";
					if (columns.get(i).getClass().getName() != "java.lang.Integer") {
						System.out.println(columns.get(i).getClass().getName());
						q = q + "'" + columns.get(i) + "' AND ";
					} else {
						q = q + columns.get(i) + " AND ";
					}
				}
			}
			q = q.substring(0, q.length() - 5);
			System.out.println("checking query if rating exists");
			System.out.println(q);
			PreparedStatement query = conn.prepareStatement(q);
			ResultSet r = query.executeQuery();
			while (r.next()) {
				int num = r.getInt(1);
				System.out.println("Rating exists?");
				System.out.println(num);
				if (num == 0) {
					System.out.println("updating");
					query = conn.prepareStatement("INSERT INTO " + this.table + suffix + "(" + StringUtils.join(columnNames, ",") + ") VALUES (" + StringUtils.join(columns, ",") + ");");
					System.out.println(columns);
					query.executeUpdate();
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
