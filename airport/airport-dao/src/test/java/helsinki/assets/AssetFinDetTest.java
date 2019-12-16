package helsinki.assets;

import static helsinki.assets.AssetDao.ERR_FAILED_SAVE;
import static helsinki.assets.IAsset.DEFAULT_ASSET_NUMBER;
import static helsinki.assets.validators.AssetFinDetAcquireDateWithinProjectPeriodValidator.ERR_OUTSIDE_PROJECT_PERIOD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.cond;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.expr;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchAggregates;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchAll;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchAllAndInstrument;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchAllInclCalc;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchAllInclCalcAndInstrument;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchAndInstrument;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchIdOnly;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchKeyAndDescOnly;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchKeyAndDescOnlyAndInstrument;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchOnly;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchOnlyAndInstrument;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.from;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.orderBy;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.select;
import static ua.com.fielden.platform.utils.EntityUtils.fetch;

import java.util.List;

import org.junit.Test;

import helsinki.assets.validators.AssetFinDetAcquireDateWithinProjectPeriodValidator;
import helsinki.projects.Project;
import helsinki.tablecodes.assets.AssetClass;
import helsinki.test_config.AbstractDaoTestCase;
import helsinki.test_config.UniversalConstantsForTesting;
import ua.com.fielden.platform.dao.QueryExecutionModel;
import ua.com.fielden.platform.dao.exceptions.EntityAlreadyExists;
import ua.com.fielden.platform.entity.query.fluent.fetch;
import ua.com.fielden.platform.entity.query.model.EntityResultQueryModel;
import ua.com.fielden.platform.entity.query.model.OrderingModel;
import ua.com.fielden.platform.error.Result;
import ua.com.fielden.platform.types.Money;
import ua.com.fielden.platform.utils.IUniversalConstants;

/**
 * This is a test case for {@link AssetClass}.
 * 
 * @author TG-Demo-Team
 *
 */
public class AssetFinDetTest extends AbstractDaoTestCase {

    @Test 
    public void acquired_date_cannot_be_outside_of_project_with_open_period() {
        final Asset asset = save(new_(Asset.class).setDesc("a demo asset"));
        final Project project = save(new_(Project.class).setName("PROJECT 1").setStartDate(date("2019-12-08 00:00:00")).setDesc("project description"));
        
        final AssetFinDet finDet = co$(AssetFinDet.class).findById(asset.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel());
        finDet.setProject(project);
        assertTrue(finDet.isValid().isSuccessful());
        
        finDet.setAcquireDate(date("2019-12-10 00:00:00"));
        assertTrue(finDet.isValid().isSuccessful());
        
        finDet.setAcquireDate(date("2019-10-10 00:00:00"));
        assertFalse(finDet.isValid().isSuccessful());
        assertEquals(date("2019-12-10 00:00:00"), finDet.getAcquireDate());
        assertEquals(ERR_OUTSIDE_PROJECT_PERIOD, finDet.isValid().getMessage());
    }

    @Test
    public void acquired_date_cannot_be_outside_of_project_period_with_closed_perdiod() {
        final Asset asset = save(new_(Asset.class).setDesc("a demo asset"));
        final Project project = save(new_(Project.class).setName("PROJECT 1")
                .setStartDate(date("2019-12-08 00:00:00"))
                .setFinishDate(date("2020-12-08 00:00:00"))
                .setDesc("project description"));
        
        final AssetFinDet finDet = co$(AssetFinDet.class).findById(asset.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel());
        finDet.setProject(project);
        assertTrue(finDet.isValid().isSuccessful());
        
        finDet.setAcquireDate(date("2019-12-10 00:00:00"));
        assertTrue(finDet.isValid().isSuccessful());
        
        finDet.setAcquireDate(date("2020-12-10 00:00:00"));
        assertFalse(finDet.isValid().isSuccessful());
        assertEquals(date("2019-12-10 00:00:00"), finDet.getAcquireDate());
        assertEquals(ERR_OUTSIDE_PROJECT_PERIOD, finDet.isValid().getMessage());
    }

    @Test 
    public void acquired_date_is_revalidate_upon_project_change() {
        final Asset asset = save(new_(Asset.class).setDesc("a demo asset"));
        final Project project = save(new_(Project.class).setName("PROJECT 1").setStartDate(date("2019-12-08 00:00:00")).setDesc("project description"));
        
        final AssetFinDet finDet = co$(AssetFinDet.class).findById(asset.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel());
        finDet.setAcquireDate(date("2019-12-05 00:00:00"));
        assertTrue(finDet.isValid().isSuccessful());
        
        finDet.setProject(project);
        assertFalse(finDet.isValid().isSuccessful());
        assertEquals(project, finDet.getProject());
        assertEquals(ERR_OUTSIDE_PROJECT_PERIOD, finDet.isValid().getMessage());
    }

    @Test 
    public void asset_fin_det_is_created_and_saved_at_the_same_time_as_asset() {
        final Asset asset = save(new_(Asset.class).setDesc("a demo asset"));
        assertTrue(co(Asset.class).entityExists(asset));
        assertTrue(co(AssetFinDet.class).entityExists(asset.getId()));
    }

    @Test 
    public void no_fin_det_is_created_and_saved_when_existing_asset_gets_saved() {
        final Asset asset = save(new_(Asset.class).setDesc("a demo asset"));
        assertEquals(Long.valueOf(0), asset.getVersion());

        final AssetFinDet finDet = co(AssetFinDet.class).findById(asset.getId());
        assertEquals(Long.valueOf(0), finDet.getVersion());

        assertEquals(Long.valueOf(1), save(asset.setDesc("another description")).getVersion());
        assertEquals(Long.valueOf(0), co(AssetFinDet.class).findById(finDet.getId()).getVersion());
    }
    
    @Test
    public void duplicate_fin_det_for_the_same_asset_is_not_permited() {
        final Asset asset = save(new_(Asset.class).setDesc("a demo asset"));
        final AssetFinDet newFinDet = new_(AssetFinDet.class).setKey(asset);
        try {
            save(newFinDet);
            fail("Should have failed due to duplicate instances.");
        } catch(final EntityAlreadyExists ex) {
        }
    }

    @Test
    public void empty_acquire_date_is_defaulted_to_project_start_date_upon_project_assignment() {
        final Asset asset = save(new_(Asset.class).setDesc("a demo asset"));
        final AssetFinDet finDet = co$(AssetFinDet.class).findById(asset.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel());
        assertNull(finDet.getAcquireDate());
        assertNull(finDet.getProject());
        
        final Project newProject = save(new_(Project.class).setName("PROJECT 1").setStartDate(date("2019-12-08 00:00:00")).setDesc("Project description"));
        final Project project = co(Project.class).findById(newProject.getId(), IAssetFinDet.FETCH_PROVIDER.<Project>fetchFor("project").fetchModel());
        finDet.setProject(project);
        assertEquals(date("2019-12-08 00:00:00"), finDet.getAcquireDate());
    }

    @Test
    public void non_empty_acquire_date_is_not_changed_to_project_start_date_upon_project_assignment() {
        final Asset asset = save(new_(Asset.class).setDesc("a demo asset"));
        final AssetFinDet finDet = save(co$(AssetFinDet.class).findById(asset.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel()).setAcquireDate(date("2019-12-10 00:00:00")));
        assertNotNull(finDet.getAcquireDate());
        assertNull(finDet.getProject());
        
        final Project newProject = save(new_(Project.class).setName("PROJECT 1").setStartDate(date("2019-12-08 00:00:00")).setDesc("Project description"));
        final Project project = co(Project.class).findById(newProject.getId(), IAssetFinDet.FETCH_PROVIDER.<Project>fetchFor("project").fetchModel());
        finDet.setProject(project);
        assertEquals(date("2019-12-10 00:00:00"), finDet.getAcquireDate());
        assertFalse(finDet.getProperty("acquireDate").isDirty());
    }

    @Test
    public void acquire_date_does_not_get_mutated_upon_fin_det_retrieval() {
        final Project project = save(new_(Project.class).setName("PROJECT 1").setStartDate(date("2019-12-08 00:00:00")).setDesc("Project description"));
        final Asset asset = save(new_(Asset.class).setDesc("a demo asset"));
        final AssetFinDet finDet = save(co$(AssetFinDet.class).findById(asset.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel())
                .setAcquireDate(date("2019-12-10 00:00:00"))
                .setProject(project));
        assertNotNull(finDet.getAcquireDate());
        assertNotNull(finDet.getProject());
        
        assertFalse(finDet.getProperty("acquireDate").isDirty());
        assertFalse(finDet.getProperty("project").isDirty());
        
        assertEquals(date("2019-12-10 00:00:00"), finDet.getAcquireDate());
        assertEquals(date("2019-12-08 00:00:00"), finDet.getProject().getStartDate());
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
