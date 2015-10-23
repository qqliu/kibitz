package kibitz;

import kibitz.RecommenderService.Processor;

import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.server.TServlet;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * KibitzServlet: Set up TJSON communication with
 * clients.
 *
 * @author Quanquan Liu
 *
 * @date 10/22/2014
*/

public class KibitzServlet extends TServlet {
	private static final long serialVersionUID = 1L;

	public KibitzServlet(MysqlDataSource dataSource)	{
		super(extracted(dataSource), new TJSONProtocol.Factory());
	}

	private static Processor<KibitzServer> extracted(MysqlDataSource dataSource) {
		return new RecommenderService.Processor<KibitzServer>(new KibitzServer(dataSource));
	}
}
