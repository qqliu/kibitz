package kibitz;

import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.server.TServlet;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class KibitzServlet extends TServlet {
	public KibitzServlet(MysqlDataSource dataSource)	{
		super(new RecommenderService.Processor(new IndividualRecommender(dataSource)), new TJSONProtocol.Factory());
	}
}
