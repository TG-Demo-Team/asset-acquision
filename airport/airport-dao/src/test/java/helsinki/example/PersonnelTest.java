package helsinki.example;

import static org.junit.Assert.*;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetch;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.from;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.orderBy;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.select;

import java.math.BigDecimal;

import org.junit.Test;

import ua.com.fielden.platform.dao.QueryExecutionModel;
import ua.com.fielden.platform.entity.query.fluent.fetch;
import ua.com.fielden.platform.entity.query.model.EntityResultQueryModel;
import ua.com.fielden.platform.entity.query.model.OrderingModel;
import ua.com.fielden.platform.security.user.User;
import ua.com.fielden.platform.utils.IUniversalConstants;
import helsinki.personnel.Person;
import helsinki.test_config.AbstractDaoTestCase;
import helsinki.test_config.UniversalConstantsForTesting;

/**
 * This is an example unit test, which can be used as a starting point for creating application unit tests.
 * 
 * @author Generated
 *
 */
public class PersonnelTest extends AbstractDaoTestCase {

    /**
     * The names of the test method should be informative. 
     * It is recommended to make the method name sound like a sentence stating the expected behaviour.
     * In this case, the test method name indicates that it is expected to find person with initials RDM and that it has an active status.
     * <p> 
     * Each test method should be related to exactly one concern, which facilitates creation of unit tests that address a single concern.
     */
    @Test
    public void user_RMD_is_present_and_active() {
    	final Person person = co(Person.class).findByKey("RMD");
    	assertNotNull(person);
    	assertTrue(person.isActive());
    }

    @Test
    public void user_JC_is_present_but_not_active() {
        final Person person = co(Person.class).findByKey("JC");
        assertNotNull(person);
        assertFalse(person.isActive());
    }

    /**
     * In case of a complex data population it is possible to store the data into a script by changing this method to return <code>true</code>.
     * <p>
     * This way it is possible to reuse it later in place of re-running the data population logic, which is a lot faster.
     * Please also refer method {@link #useSavedDataPopulationScript()} below -- the values returned by this and that mathod cannot be <code>true</code> simultaneously.
     */
    @Override
    public boolean saveDataPopulationScriptToFile() {
        return false;
    }

    /**
     * If the test data was populated and saved as a script file (hinted in method {@link #saveDataPopulationScriptToFile()} above),
     * then this method can be changed to return <code>true</code> in order to avoid execution of the data population logic and simply execute the saved script.
     * This makes the population of the test data a lot faster.
     * It is very convenient when there is a need to run the same test case multiple times interactivelly.
     * <p>
     * However, this method should never return <code>true</code> when running multiple test cases.
     * Therefore, it is imporant to change this method to return <code>false</code> before committing changes into your VCS such as Git.
     */
    @Override
    public boolean useSavedDataPopulationScript() {
        return false;
    }

    /**
     * Domain state population method.
     * <p>
     * <b>IMPORTANT:</p> this method executes only once for a Test Case. At the same time, new instances of a Test Case are created for each test method.
     * Thus, this method should not be used for initialisation of the Test Case state other than the persisted domain state.
     */
    @Override
    protected void populateDomain() {
        // Need to invoke super to create a test user that is responsible for data population 
    	super.populateDomain();

    	// Here is how the Test Case universal constants can be set.
    	// In this case the notion of now is overridden, which makes it possible to have an invariant system-time.
    	// However, the now value should be after AbstractDaoTestCase.prePopulateNow in order not to introduce any date-related conflicts.
    	final UniversalConstantsForTesting constants = (UniversalConstantsForTesting) getInstance(IUniversalConstants.class);
    	constants.setNow(dateTime("2019-10-01 11:30:00"));

    	// If the use of saved data population script is indicated then there is no need to proceed with any further data population logic.
        if (useSavedDataPopulationScript()) {
            return;
        }

    	// Here the three Person entities are persisted using the the inherited from TG testing framework methods.
        save(new_(Person.class, "RMD", "Ronald McDonald").setActive(true));
        save(new_(Person.class, "JC", "John Carmack").setActive(false));
    }

}
