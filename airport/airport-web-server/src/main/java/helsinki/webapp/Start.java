package helsinki.webapp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.restlet.data.Parameter;
import org.restlet.data.Protocol;
import org.restlet.util.Series;

/**
 * Jetty launching class. Configure to start a web application as a standalone Jetty server.
 *
 * @author Generated
 *
 */
public class Start {

    private static final Logger LOGGER = Logger.getLogger(Start.class);

    public static void main(final String[] args) throws IOException {
        final String fileName = args.length == 1 ? args[0] : "application.properties";
        final Properties props = new Properties();
        try (final InputStream st = new FileInputStream(fileName);) {
            props.load(st);
        }
        
        DOMConfigurator.configure(props.getProperty("log4j"));

        LOGGER.info("Starting...");
        final ApplicationConfiguration app = new ApplicationConfiguration(props);

        final org.restlet.Server server = app.getServers().add(Protocol.HTTPS, Integer.parseInt(props.getProperty("port.listen")));
        final Series<Parameter> parameters = server.getContext().getParameters();
        // TLS related parameters to establish HTTPS connections
        parameters.add("sslContextFactory", "org.restlet.engine.ssl.DefaultSslContextFactory");
        parameters.add("keystorePath", props.getProperty("web.keystore.path"));
        parameters.add("keystorePassword", "changeit");
        parameters.add("keyPassword", "changeit");
        parameters.add("keystoreType", "JKS");

        // thread pool parameters for handling requests
        parameters.add("threadPool.minThreads", "10");
        parameters.add("threadPool.maxThreads", "200");
        parameters.add("threadPool.idleTimeout", "60000");

        try {
            app.start();
            LOGGER.info("started");
        } catch (final Exception ex) {
            LOGGER.fatal(ex);
            System.exit(100);
        }
    }
}