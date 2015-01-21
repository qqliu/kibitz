package kibitz;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class EmbeddingJettyWithServlet {
	
	public static void main(String[] args) throws Exception {
		Server server = new Server(9888);
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/kibitz");

		// Enable CORS - cross origin resource sharing (for http and https)
		FilterHolder cors = new FilterHolder();
		cors.setInitParameter("allowedOrigins", "*");
		cors.setInitParameter("allowedHeaders", "*");
		cors.setInitParameter("allowedMethods", "GET, POST");
		cors.setFilter(new CrossOriginFilter());
		context.addFilter(cors, "*", EnumSet.of(DispatcherType.REQUEST, DispatcherType.ASYNC, DispatcherType.INCLUDE));
		
		server.setHandler(context);
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setServerName("http://datahub.csail.mit.edu/service");
		dataSource.setDatabaseName("quanquan.books");
		context.addServlet(new ServletHolder(new KibitzServlet(dataSource)), "/*");
		
		server.start();
		server.join();
	}
}
