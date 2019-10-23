package helsinki.test_config;

import java.util.Locale;

import org.joda.time.DateTime;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import ua.com.fielden.platform.utils.IUniversalConstants;

/**
 * A convenient implementation of the {@link IUniversalConstants} contract to provide flexible notion of the <code>now</code> for unit tests. 
 * 
 * @author Generated
 *
 */
public class UniversalConstantsForTesting implements IUniversalConstants {

    private DateTime now;
    private final String appName;
    private final String smtpServer;
    private final String fromEmailAddress;

    @Inject
    public UniversalConstantsForTesting(
            final @Named("app.name") String appName,
            final @Named("email.smtp") String smtpServer,
            final @Named("email.fromAddress") String fromEmailAddress) {
        this.appName = appName;
        this.smtpServer = smtpServer;
        this.fromEmailAddress = fromEmailAddress;
    }

    @Override
    public DateTime now() {
        return now;
    }

    @Override
    public Locale locale() {
        return Locale.getDefault();
    }

    public DateTime getNow() {
        return now;
    }

    public void setNow(final DateTime now) {
        this.now = now;
    }

    @Override
    public String appName() {
        return appName;
    }

    @Override
    public String smtpServer() {
        return smtpServer;
    }

    @Override
    public String fromEmailAddress() {
        return fromEmailAddress;
    }
}
