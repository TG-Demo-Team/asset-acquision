package helsinki.test_config;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.runner.RunWith;

import ua.com.fielden.platform.entity.AbstractEntity;
import ua.com.fielden.platform.test.AbstractDomainDrivenTestCase;
import ua.com.fielden.platform.utils.IUniversalConstants;
import ua.com.fielden.platform.security.user.User;

import helsinki.config.ApplicationDomain;
import helsinki.personnel.Person;
import helsinki.test_config.UniversalConstantsForTesting;

/**
 * This calss should be used as the base type for all domain-driven test cases. 
 * Its main purpose is to be a common layer for providing functionaity common to most if not all test cases, and to be easily extensible if application changes are needed.
 * 
 * @author Generated
 *
 */
@RunWith(H2DomainDrivenTestCaseRunner.class)
public abstract class AbstractDaoTestCase extends AbstractDomainDrivenTestCase {

    public static final String TEST_PERSON_KEY = User.system_users.UNIT_TEST_USER.name();
    public final DateTime prePopulateNow = dateTime("2019-01-01 08:00:00");

    private final ApplicationDomain applicationDomainProvider = new ApplicationDomain();

    @Override
    protected final List<Class<? extends AbstractEntity<?>>> domainEntityTypes() {
        return applicationDomainProvider.entityTypes();
    }

    /**
     * Initialises a test user. Needs to be invoked in descendent classes.
     */
    @Override
    protected void populateDomain() {
        final UniversalConstantsForTesting constants = (UniversalConstantsForTesting) getInstance(IUniversalConstants.class);
        constants.setNow(prePopulateNow);

        setupUser(User.system_users.UNIT_TEST_USER, "unit.test.user");
        setupPerson(User.system_users.UNIT_TEST_USER, "unit.test.user");
    }
    
    private void setupPerson(final User.system_users defaultUser, final String emailDomain) {
        final User su = co(User.class).findByKey(defaultUser.name());
        save(new_(Person.class, defaultUser.name()).setActive(true).setUser(su).setDesc("Person who is a user").setEmail(defaultUser + "@" + emailDomain));
    }

}

