package generalrecommender;

import generalrecommender.RecommenderService.Iface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class IndividualRecommender implements Iface {
	private String file;
	private DataModel model;
	private ReloadFromJDBCDataModel dataModel;
	
	public IndividualRecommender(String file, ReloadFromJDBCDataModel dataModel) {
		this.file = file;
		this.dataModel = dataModel;
	}
	
	public List<Long> makeRecommendation(int numRecs) {
		try {
			if (this.dataModel == null) {
				model = new FileDataModel(new File(this.file));
			} else {
				model = this.dataModel;
			}
			UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(model);
			UserNeighborhood neighborhood =
				      new NearestNUserNeighborhood(3, userSimilarity, model);
			Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, userSimilarity);
			Recommender cachingRecommender = new CachingRecommender(recommender);
			List<RecommendedItem> recommendations = cachingRecommender.recommend(3, numRecs);
			ArrayList<Long> recommendationNames = new ArrayList<Long>();
			for (int i = 0; i < recommendations.size(); i++) {
				recommendationNames.add(recommendations.get(i).getItemID());
			}
			return recommendationNames;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TasteException e) {
			e.printStackTrace();
		}
		return null;
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
	}
	
	public void saveIntoDb(List<String> columns) {
		try {
			Connection conn = (Connection) this.dataModel.getDataSource().getConnection();
			Statement query = conn.createStatement();
			query.executeUpdate("INSERT INTO movies VALUES (" + StringUtils.join(columns, ",") + ");");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	public static void main (String[] args) {
		 /*IndividualRecommender recommender = new IndividualRecommender("data.txt");
		 List<RecommendedItem> recommendations = recommender.makeRecommendation(20);
		 System.out.println(recommendations);*/

        try {
            //IndividualRecommender recommender = new IndividualRecommender("data.txt", null);
        	MysqlDataSource dataSource = new MysqlDataSource();
    		dataSource.setServerName("sql.mit.edu");
    		dataSource.setUser("quanquan");
    		dataSource.setPassword("hof9924ne@!");
    		dataSource.setDatabaseName("quanquan+datahub");
    		ReloadFromJDBCDataModel model = new ReloadFromJDBCDataModel(new MySQLJDBCDataModel(dataSource, "movies", "user_id", "item_id", "ratings", null)); 
    		IndividualRecommender recommender = new IndividualRecommender("data.txt", model);
    		//recommender.readFromFile();
            TServerSocket serverTransport = new TServerSocket(9888);
            RecommenderService.Processor<IndividualRecommender> processor =
                new RecommenderService.Processor<IndividualRecommender>(recommender);
            Args args1 = new Args(serverTransport);
            args1.processor(processor);
            // TNonblockingServer might be a better idea to prevent multiple windows from 
            // blocking the server until one process finishes
            TServer server = new TThreadPoolServer(args1);
 
            System.out.println("Started service successfully...");
            server.serve();
        } catch(TTransportException e) {
            e.printStackTrace();
        } catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
