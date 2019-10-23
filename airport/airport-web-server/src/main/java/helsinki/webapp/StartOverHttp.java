package helsinki.webapp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.restlet.Component;
import org.restlet.data.Parameter;
import org.restlet.data.Protocol;
import org.restlet.util.Series;

import ua.com.fielden.platform.entity.AbstractEntity;
/**
 * Jetty launching class. Configure to start a web application as a standalone Jetty server over HTTP.
 * Designed to be used when running the application from behind HAProxy.
 *
 * @author Generated
 *
 */
public class StartOverHttp {

    private static final Logger LOGGER = Logger.getLogger(StartOverHttp.class);

    private StartOverHttp() {}

    public static void main(final String[] args) throws IOException {
        final String fileName = args.length == 1 ? args[0] : "application.properties";
        final Properties props = new Properties();
        try (final InputStream st = new FileInputStream(fileName);) {
            props.load(st);
        }
        
        DOMConfigurator.configure(props.getProperty("log4j"));

        /*
         * Let's use non-strict-model verification for instrumented entities in deployment.
         */
        AbstractEntity.useNonStrictModelVerification();

        LOGGER.info("Starting with HTTP...");
        final ApplicationConfiguration app = new ApplicationConfiguration(props);

        final org.restlet.Server server = app.getServers().add(Protocol.HTTP, Integer.parseInt(props.getProperty("port.listen")));
        final Series<Parameter> parameters = server.getContext().getParameters();
        // thread pool parameters for handling requests
        parameters.add("threadPool.minThreads", "10");
        parameters.add("threadPool.maxThreads", "200");
        parameters.add("threadPool.idleTimeout", "60000");

        // set up cleanup and access audit filters

        try {
            app.start();
            LOGGER.info("Started app server.");
        } catch (final Exception ex) {
            LOGGER.fatal(ex);
            System.exit(100);
        }
    }
}