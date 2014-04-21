package kibitz;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class EmbeddingJettyWithServlet {
	
	public static void main(String[] args) throws Exception {
		Server server = new Server(9888);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/kibitz");
		server.setHandler(context);
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setServerName("sql.mit.edu");
		dataSource.setUser("quanquan");
		dataSource.setPassword("hof9924ne@!");
		dataSource.setDatabaseName("quanquan+datahub");
		context.addServlet(new ServletHolder(new KibitzServlet(dataSource)), "/*");
		server.start();
	}
}
