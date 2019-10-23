package helsinki.ioc;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.inject.Injector;
import com.google.inject.binder.AnnotatedBindingBuilder;

import helsinki.webapp.WebUiConfig;
import ua.com.fielden.platform.basic.config.IApplicationDomainProvider;
import ua.com.fielden.platform.basic.config.Workflows;
import ua.com.fielden.platform.entity.AbstractEntity;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.security.NoAuthorisation;
import ua.com.fielden.platform.serialisation.api.ISerialisationClassProvider;
import ua.com.fielden.platform.web.ioc.IBasicWebApplicationServerModule;

/**
 * Guice injector module for Helsinki Airport Asset Management server.
 *
 * @author TG Team
 *
 */
public class WebApplicationServerModule extends ApplicationServerModule implements IBasicWebApplicationServerModule {

    private final String domainName;
    private final String path;
    private final int port;
    private final Workflows workflow;

    public WebApplicationServerModule(
            final Map<Class, Class> defaultHibernateTypes,
            final IApplicationDomainProvider applicationDomainProvider,
            final List<Class<? extends AbstractEntity<?>>> domainTypes,
            final Class<? extends ISerialisationClassProvider> serialisationClassProviderType,
            final Class<? extends IFilter> automaticDataFilterType,
            final Properties props) throws Exception {
        super(defaultHibernateTypes, applicationDomainProvider, domainTypes, serialisationClassProviderType, automaticDataFilterType, NoAuthorisation.class, props);
        this.domainName = props.getProperty("web.domain");
        this.port = Integer.valueOf(props.getProperty("port"));
        this.path = props.getProperty("web.path");

        this.workflow = Workflows.valueOf(props.getProperty("workflow"));
    }

    @Override
    protected void configure() {
        super.configure();
        bindWebAppResources(new WebUiConfig(domainName, port, workflow, path));
    }

    @Override
    public void setInjector(final Injector injector) {
        super.setInjector(injector);
        initWebApp(injector);
    }

    @Override
    public <T> AnnotatedBindingBuilder<T> bindType(final Class<T> clazz) {
        return bind(clazz);
    }
}
