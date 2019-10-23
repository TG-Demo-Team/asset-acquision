package helsinki.test_config;

import java.util.Properties;

import org.apache.log4j.xml.DOMConfigurator;

import ua.com.fielden.platform.entity.factory.EntityFactory;
import ua.com.fielden.platform.entity.query.IdOnlyProxiedEntityTypeCache;
import ua.com.fielden.platform.entity.query.metadata.DomainMetadata;
import ua.com.fielden.platform.ioc.ApplicationInjectorFactory;
import ua.com.fielden.platform.ioc.NewUserNotifierMockBindingModule;
import ua.com.fielden.platform.security.NoAuthorisation;
import ua.com.fielden.platform.test.DbDrivenTestCase;
import ua.com.fielden.platform.test.IDomainDrivenTestCaseConfiguration;

import com.google.inject.Injector;

import helsinki.config.ApplicationDomain;
import helsinki.dbsetup.HibernateSetup;
import helsinki.filter.NoDataFilter;
import helsinki.ioc.ApplicationServerModule;
import helsinki.serialisation.SerialisationClassProvider;

/**
 * Provides implementation of {@link IDomainDrivenTestCaseConfiguration} for testing purposes, which is mainly related to construction of appropriate IoC modules.
 *
 * @author Generated
 *
 */
public final class DaoDomainDrivenTestCaseConfiguration implements IDomainDrivenTestCaseConfiguration {
    private final EntityFactory entityFactory;
    private final Injector injector;
    private final ApplicationServerModule iocModule;

    /**
     * Required for dynamic instantiation by {@link DbDrivenTestCase}
     */
    public DaoDomainDrivenTestCaseConfiguration(final Properties props) {
    	try {
    	    DOMConfigurator.configure("src/test/resources/log4j.xml");
    	    // application properties
    	    props.setProperty("app.name", "Helsinki Airport Asset Management");
    	    props.setProperty("email.smtp", "non-existing-server");
    	    props.setProperty("email.fromAddress", "airport_support@helsinki.com.ua");
    	    props.setProperty("reports.path", "");
    	    props.setProperty("domain.path", "../airport-pojo-bl/target/classes");
    	    props.setProperty("domain.package", "helsinki");
    	    props.setProperty("tokens.path", "../airport-pojo-bl/target/classes");
    	    props.setProperty("tokens.package", "helsinki.security.tokens");
            props.setProperty("attachments.location", "../airport-web-server/src/test/resources/attachments");
    	    props.setProperty("workflow", "development");
    	    // custom Hibernate configuration properties
    	    props.setProperty("hibernate.show_sql", "false");
    	    props.setProperty("hibernate.format_sql", "true");
    	    props.setProperty("cacheDefaults", "false");
    	    
    	    final ApplicationDomain applicationDomainProvider = new ApplicationDomain();
    	    
            iocModule = new ApplicationServerModule(
                    HibernateSetup.getHibernateTypes(),
                    applicationDomainProvider,
                    applicationDomainProvider.domainTypes(),
                    SerialisationClassProvider.class,
                    NoDataFilter.class,
                    NoAuthorisation.class,
                    UniversalConstantsForTesting.class,
                    props);
    
    	    injector = new ApplicationInjectorFactory()
                    .add(iocModule)
                    .add(new NewUserNotifierMockBindingModule())
                    .getInjector();
    
            entityFactory = injector.getInstance(EntityFactory.class);
    	} catch (final Exception e) {
    	    e.printStackTrace();
    	    throw new RuntimeException(e);
    	}
    }

    @Override
    public EntityFactory getEntityFactory() {
        return entityFactory;
    }

    @Override
    public <T> T getInstance(final Class<T> type) {
        return injector.getInstance(type);
    }

    @Override
    public DomainMetadata getDomainMetadata() {
        return iocModule.getDomainMetadata();
    }

    @Override
    public IdOnlyProxiedEntityTypeCache getIdOnlyProxiedEntityTypeCache() {
        return iocModule.getIdOnlyProxiedEntityTypeCache();
    }
}
