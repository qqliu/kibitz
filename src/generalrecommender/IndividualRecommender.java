package kibitz;

import kibitz.RecommenderService.Iface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.lang.StringUtils;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class IndividualRecommender implements Iface {
	private ReloadFromJDBCDataModel dataModel;
	private MysqlDataSource dataSource;
	private String table;
	
	public IndividualRecommender(MysqlDataSource dataSource) {
		this.dataModel = null;
		this.dataSource = dataSource;
	}
	
	public List<List<String>> makeRecommendation(int userId, int numRecs) {
		try {
			if (this.dataModel != null) {
				UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(this.dataModel);
				UserNeighborhood neighborhood =
				new NearestNUserNeighborhood(3, userSimilarity, this.dataModel);
				Recommender recommender = new GenericUserBasedRecommender(this.dataModel, neighborhood, userSimilarity);
				Recommender cachingRecommender = new CachingRecommender(recommender);
				List<RecommendedItem> recommendations = cachingRecommender.recommend(userId, numRecs);
				ArrayList<Long> recommendationNames = new ArrayList<Long>();
				for (int i = 0; i < recommendations.size(); i++) {
					recommendationNames.add(recommendations.get(i).getItemID());
				}
				Connection conn = this.dataSource.getConnection();
				Statement query = conn.createStatement();
				List<List<String>> items = new ArrayList<List<String>>();
				if (recommendationNames.size() == 0) {
					ResultSet results = query.executeQuery("SELECT * FROM " + this.table + " ORDER BY RAND() LIMIT " + numRecs + ";");
					System.out.println(results);
					while(results.next()){
						List<String> item = new ArrayList<String>();
						item.add(Integer.toString(results.getInt(0)));
						item.add(results.getString(1));
						items.add(item);
					}
				} else {
					for (int i = 0; i < recommendationNames.size(); i++) {
						List<String> item = new ArrayList<String>();
						Long itemId = recommendationNames.get(i);
						System.out.println(recommendationNames);
						ResultSet results = query.executeQuery("SELECT item FROM " + this.table + " WHERE id=" + itemId + ";");
						item.add(Long.toString(itemId));
						item.add(results.getString(0));
						items.add(item);
					}
				}
				return items;
			}
		} catch (TasteException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List<List<String>> getItems() {
		List<List<String>> items = new ArrayList<List<String>>();
		try {
			Connection conn = this.dataSource.getConnection();
			Statement query = conn.createStatement();
			ResultSet results = query.executeQuery("SELECT * FROM " + this.table + ";");
			while(results.next()){
				List<String> item = new ArrayList<String>();
				item.add(Integer.toString(results.getInt(0)));
				item.add(results.getString(1));
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
		saveIntoDb(columns, "ratings");
	}
	
	public String createNewUser(String username, String email, String password) {
		List<String> columns = new ArrayList<String>();
		columns.add(username);
		columns.add(email);
		String passwordHash;
		try {
			passwordHash = generatePasswordHash(password);
			columns.add(passwordHash);
			saveIntoDb(columns, "users");
			Connection conn = this.dataSource.getConnection();
			Statement query = conn.createStatement();
			ResultSet results = query.executeQuery("SELECT id FROM " + this.table + "users WHERE username='" + username + "'");
			results.first();
			Integer userid = (Integer) results.getObject(0); 
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
	
	public boolean checkLogin(String username, String password) {
		Connection conn;
		try {
			conn = this.dataSource.getConnection();
			Statement query = conn.createStatement();
			ResultSet results = query.executeQuery("SELECT password FROM " + this.table + "users WHERE username='" + username + "'");
			results.first();
			String hash = (String) results.getObject(0); 
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
	
	public void initiateModel(String table) {
		try {
			this.dataModel = new ReloadFromJDBCDataModel(new MySQLJDBCDataModel(this.dataSource, table+"ratings", "user_id", "item_id", "rating", null));
			this.table = table;
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	/*public void readFromFile() {
	 BufferedReader reader;
	 try {
	 reader = new BufferedReader(new FileReader(this.file));
	 String line = null;
	 while ((line = reader.readLine()) != null) {
	 final StringTokenizer tokenizer = new StringTokenizer(line, ",");
	 final List<String> columns = new ArrayList<String>();
	 while (tokenizer.hasMoreTokens()) {
	 columns.add(tokenizer.nextToken());
	 }
	 saveIntoDb(columns);
	 }
	 reader.close();
	 } catch (FileNotFoundException e1) {
	 // TODO Auto-generated catch block
	 e1.printStackTrace();
	 } catch (IOException e) {
	 throw new IllegalArgumentException("Error reading file (" + this.file + ")", e);
	 }
	 }*/
	
	private void saveIntoDb(List<?> columns, String suffix) {
		try {
			Connection conn = this.dataSource.getConnection();
			Statement query = conn.createStatement();
			query.executeUpdate("INSERT INTO " + this.table + suffix + " VALUES (" + StringUtils.join(columns, ",") + ");");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main (String[] args) {
		/*IndividualRecommender recommender = new IndividualRecommender("data.txt");
		 List<RecommendedItem> recommendations = recommender.makeRecommendation(20);
		 System.out.println(recommendations);*/
		
        //try {
		//IndividualRecommender recommender = new IndividualRecommender("data.txt", null);
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setServerName("sql.mit.edu");
		dataSource.setUser("quanquan");
		dataSource.setPassword("hof9924ne@!");
		dataSource.setDatabaseName("quanquan+datahub");
		IndividualRecommender recommender = new IndividualRecommender(dataSource);
		recommender.initiateModel("f6afe418118814ae1c62aeae803ab049");
		recommender.makeRecommendation(276747, 10);
		//recommender.readFromFile();
		/*TServerSocket serverTransport = new TServerSocket(9888);
		 RecommenderService.Processor<IndividualRecommender> processor =
		 new RecommenderService.Processor<IndividualRecommender>(recommender);
		 Args args1 = new Args(serverTransport);
		 args1.processor(processor);
		 // TNonblockingServer might be a better idea to prevent multiple windows from 
		 // blocking the server until one process finishes
		 TServer server = new TThreadPoolServer(args1);*/
		
		//System.out.println("Started service successfully...");
		//server.serve();
        /*} catch(TTransportException e) {
		 e.printStackTrace();
		 }*/
	}
}
