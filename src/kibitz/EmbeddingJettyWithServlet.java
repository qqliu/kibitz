package kibitz;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class EmbeddingJettyWithServlet {
	
	public static void main(String[] args) throws Exception {
		// Setup Threadpool for multiple server connections
	    QueuedThreadPool threadPool = new QueuedThreadPool();
	    threadPool.setMaxThreads(500);

	    //ThreadPool Server
	    Server server = new Server(threadPool);
	    int port = 9889;
	    int portSecure = 9888;

	    // Configure jetty.home 
	    String home = ".";
	    
	    // HTTP Configuration
	    HttpConfiguration http_config = new HttpConfiguration();
	    http_config.setSecureScheme("https");
	    http_config.setSecurePort(portSecure);

	    // Configure Connector for http
	    ServerConnector http = new ServerConnector(server,
	            new HttpConnectionFactory(http_config));
	    http.setPort(port);
	    http.setIdleTimeout(30000);

	    // HTTPS Configuration
	    HttpConfiguration https_config = new HttpConfiguration(http_config);
	    https_config.addCustomizer(new SecureRequestCustomizer());

	    SslContextFactory sslContextFactory = new SslContextFactory();
	    sslContextFactory.setKeyStorePath(home + "/keystore.jks");
	    sslContextFactory.setKeyStorePassword("hof9924ne@!");
	    sslContextFactory.setKeyManagerPassword("hof9924ne@!");

	    ServerConnector sslConnector = new ServerConnector(server,
	            new SslConnectionFactory(sslContextFactory, "http/1.1"),
	            new HttpConnectionFactory(https_config));
	    sslConnector.setPort(portSecure);

	    server.setConnectors(new Connector[] { http, sslConnector });
        
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
