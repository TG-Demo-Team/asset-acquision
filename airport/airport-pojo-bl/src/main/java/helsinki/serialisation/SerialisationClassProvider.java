package helsinki.serialisation;

import ua.com.fielden.platform.basic.config.IApplicationDomainProvider;
import ua.com.fielden.platform.basic.config.IApplicationSettings;
import ua.com.fielden.platform.entity.AbstractEntity;
import ua.com.fielden.platform.serialisation.api.ISerialisationClassProvider;
import ua.com.fielden.platform.serialisation.api.impl.DefaultSerialisationClassProvider;

import com.google.inject.Inject;

/**
 * Application specific implementation of {@link ISerialisationClassProvider}, which is used for registering application specific types that need to be serialised as part of the data marshaling mechanism.
 * <p>
 * Please note that all domain types (i.e. types derived from {@link AbstractEntity}) are handled automatically and should not be added manually.
 * 
 * @author Generated
 * 
 */
public class SerialisationClassProvider extends DefaultSerialisationClassProvider {

    @Inject
    public SerialisationClassProvider(final IApplicationSettings settings, final IApplicationDomainProvider applicationDomain) throws Exception {
        super(settings, applicationDomain);
    }
    
}
