package kibitz;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
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
		context.addFilter(CrossOriginFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
		server.setHandler(context);
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setServerName("http://datahub.csail.mit.edu/service");
		dataSource.setDatabaseName("quanquan.books");
		context.addServlet(new ServletHolder(new KibitzServlet(dataSource)), "/*");
		
		server.start();
		server.join();
	}
}
