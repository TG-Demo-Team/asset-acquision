package helsinki.projects;

import static helsinki.projects.validators.ProjectStartAndFinishDatesValidator.ERR_OUTSIDE_NEW_PERIOD_DUE_TO_FINISH_DATE;
import static helsinki.projects.validators.ProjectStartAndFinishDatesValidator.ERR_OUTSIDE_NEW_PERIOD_DUE_TO_START_DATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import helsinki.assets.Asset;
import helsinki.assets.AssetFinDet;
import helsinki.assets.IAssetFinDet;
import helsinki.projects.validators.ProjectStartAndFinishDatesValidator;
import helsinki.tablecodes.assets.AssetClass;
import helsinki.test_config.AbstractDaoTestCase;
import helsinki.test_config.UniversalConstantsForTesting;
import ua.com.fielden.platform.error.Result;
import ua.com.fielden.platform.utils.IUniversalConstants;

/**
 * This is a test case for {@link AssetClass}.
 * 
 * @author TG-Demo-Team
 *
 */
public class ProjectTest extends AbstractDaoTestCase {

    @Test
    public void project_start_and_finish_do_not_permit_inverted_periods() {
        final Project project = co(Project.class).new_().setName("PROJECT 1").setDesc("Project 1 description");
        project.setStartDate(date("2019-12-08 00:00:00")).setFinishDate(date("2019-10-08 00:00:00"));
        
        assertFalse(project.isValid().isSuccessful());
        assertTrue(project.getProperty("startDate").isValid());
        assertFalse(project.getProperty("finishDate").isValid());
        assertNull(project.getFinishDate());
        
        project.setStartDate(date("2019-09-08 00:00:00"));
        assertTrue(project.getProperty("startDate").isValid());
        assertTrue(project.getProperty("finishDate").isValid());
        assertEquals(date("2019-09-08 00:00:00"), project.getStartDate());
        assertEquals(date("2019-10-08 00:00:00"), project.getFinishDate());
        assertTrue(project.isValid().isSuccessful());
        
        final Project savedProject = save(project);
        assertTrue(co(Project.class).entityExists(savedProject));
    }
    
    @Test
    public void start_date_is_requried_for_projects() {
        final Project project = co(Project.class).new_().setName("PROJECT 1").setDesc("Project 1 description");
        final Result validationResult = project.isValid();
        assertFalse(validationResult.isSuccessful());
        assertEquals("Required property [Start Date] is not specified for entity [Project].", validationResult.getMessage());
        
        project.setStartDate(date("2019-10-01 00:00:00"));
        assertTrue(project.isValid().isSuccessful());
    }

    @Test
    public void start_date_cannot_be_assigned_if_new_value_if_after_acquired_date_for_associated_assets() {
        final Project project = save(new_(Project.class).setName("PROJECT 1").setStartDate(date("2019-10-01 00:00:00")).setDesc("Project 1 description"));
        
        final Asset asset1 = save(new_(Asset.class).setDesc("first asset"));
        save(co$(AssetFinDet.class).findById(asset1.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel()).setAcquireDate(date("2019-10-02 00:00:00")).setProject(project));
        final Asset asset2 = save(new_(Asset.class).setDesc("second asset"));
        save(co$(AssetFinDet.class).findById(asset2.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel()).setAcquireDate(date("2019-11-02 00:00:00")).setProject(project));
        final Asset asset3 = save(new_(Asset.class).setDesc("third asset"));
        save(co$(AssetFinDet.class).findById(asset3.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel()).setAcquireDate(date("2020-01-02 00:00:00")).setProject(project));

        project.setStartDate(date("2019-11-01 00:00:00"));
        assertFalse(project.isValid().isSuccessful());
        assertEquals(ERR_OUTSIDE_NEW_PERIOD_DUE_TO_START_DATE, project.isValid().getMessage());
    }

    @Test
    public void finish_date_cannot_be_assigned_if_new_value_if_before_acquired_date_for_associated_assets() {
        final Project project = save(new_(Project.class).setName("PROJECT 1")
                .setStartDate(date("2019-10-01 00:00:00"))
                .setFinishDate(date("2020-10-01 00:00:00"))
                .setDesc("Project 1 description"));
        
        final Asset asset1 = save(new_(Asset.class).setDesc("first asset"));
        save(co$(AssetFinDet.class).findById(asset1.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel()).setAcquireDate(date("2019-10-02 00:00:00")).setProject(project));
        final Asset asset2 = save(new_(Asset.class).setDesc("second asset"));
        save(co$(AssetFinDet.class).findById(asset2.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel()).setAcquireDate(date("2019-11-02 00:00:00")).setProject(project));
        final Asset asset3 = save(new_(Asset.class).setDesc("third asset"));
        save(co$(AssetFinDet.class).findById(asset3.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel()).setAcquireDate(date("2020-01-02 00:00:00")).setProject(project));

        project.setFinishDate(date("2020-01-01 00:00:00"));
        assertFalse(project.isValid().isSuccessful());
        assertEquals(ERR_OUTSIDE_NEW_PERIOD_DUE_TO_FINISH_DATE, project.isValid().getMessage());
        
        project.setFinishDate(null);
        assertTrue(project.isValid().isSuccessful());
    }

    
    @Override
    public boolean saveDataPopulationScriptToFile() {
        return false;
    }

    @Override
    public boolean useSavedDataPopulationScript() {
        return false;
    }

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
    }

}
