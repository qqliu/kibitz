package kibitz;

import kibitz.RecommenderService.Processor;

import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.server.TServlet;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class KibitzServlet extends TServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public KibitzServlet(MysqlDataSource dataSource)	{
		super(extracted(dataSource), new TJSONProtocol.Factory());
	}

	private static Processor<IndividualRecommender> extracted(MysqlDataSource dataSource) {
		return new RecommenderService.Processor<IndividualRecommender>(new IndividualRecommender(dataSource));
	}
}
