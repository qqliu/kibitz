package generalrecommender;

import generalrecommender.RecommenderService.Iface;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

public class IndividualRecommender implements Iface {
	private String file;
	private DataModel model;
	
	public IndividualRecommender(String file) {
		this.file = file;
	}
	
	public List<Long> makeRecommendation(int numRecs) {
		try {
			model = new FileDataModel(new File(this.file));
			UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(model);
			UserNeighborhood neighborhood =
				      new NearestNUserNeighborhood(3, userSimilarity, model);
			Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, userSimilarity);
			Recommender cachingRecommender = new CachingRecommender(recommender);
			List<RecommendedItem> recommendations = cachingRecommender.recommend(1, numRecs);
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
	
	public void getItems(String tableName) {
		
	}
	
	public static void main (String[] args) {
		 /*IndividualRecommender recommender = new IndividualRecommender("data.txt");
		 List<RecommendedItem> recommendations = recommender.makeRecommendation(20);
		 System.out.println(recommendations);*/

        try {
            IndividualRecommender recommender = new IndividualRecommender("data.txt");
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
        }catch(TTransportException e) {
            e.printStackTrace();
        }
	}
}
