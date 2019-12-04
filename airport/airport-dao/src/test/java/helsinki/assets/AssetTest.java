package helsinki.assets;

import static helsinki.assets.AssetDao.ERR_FAILED_SAVE;
import static helsinki.assets.IAsset.DEFAULT_ASSET_NUMBER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import helsinki.tablecodes.assets.AssetClass;
import helsinki.test_config.AbstractDaoTestCase;
import helsinki.test_config.UniversalConstantsForTesting;
import ua.com.fielden.platform.dao.exceptions.EntityAlreadyExists;
import ua.com.fielden.platform.error.Result;
import ua.com.fielden.platform.utils.IUniversalConstants;

/**
 * This is a test case for {@link AssetClass}.
 * 
 * @author TG-Demo-Team
 *
 */
public class AssetTest extends AbstractDaoTestCase {

    @Test
    public void newly_saved_asset_has_number_generated() {
        final IAsset co$ = co$(Asset.class);
        final Asset asset = co$.new_().setDesc("some description");
        
        final Asset savedAsset = co$.save(asset);
        
        assertNotNull(savedAsset.getNumber());
        assertEquals("1", savedAsset.getNumber());
    }

    @Test
    public void existing_assets_keep_their_original_numbers() {
        final IAsset co$ = co$(Asset.class);
        final Asset asset = co$.new_().setDesc("some description");
        
        final Asset savedAsset = co$.save(asset).setDesc("another description");
        assertTrue(savedAsset.isDirty());
        
        final Asset savedAsset2 = co$.save(savedAsset);
        assertEquals("1", savedAsset2.getNumber());
    }

    @Test
    public void sequentially_created_assets_have_sequential_numbers() {
        final IAsset co$ = co$(Asset.class);
        final Asset asset1 = co$.save(co$.new_().setDesc("asset 1 description"));
        final Asset asset2 = co$.save(co$.new_().setDesc("asset 2 description"));
        
        assertEquals("1", asset1.getNumber());
        assertEquals("2", asset2.getNumber());
    }

    @Test
    public void new_asset_can_be_saved_after_the_first_failed_attempt() {
        final AssetDao co$ = co$(Asset.class);
        final Asset asset = co$.new_().setDesc("new desc");
        
        // the first attempt to save asset should fail
        try {
            co$.saveWithError(asset);
            fail("Should have failed the first saving attempt.");
        } catch (final Result ex) {
            assertEquals(ERR_FAILED_SAVE, ex.getMessage());
        }
        
        assertFalse(asset.isPersisted());
        assertEquals(DEFAULT_ASSET_NUMBER, asset.getNumber());
        
        final Asset savedAsset = co$.save(asset);
        assertTrue(savedAsset.isPersisted());
        assertTrue(co$.entityExists(savedAsset));
        assertEquals("1", savedAsset.getNumber());
    }
    
    @Test
    public void concurrent_saving_of_assets_is_supported_even_with_repeated_saving_after_failures() {
        final AssetDao co$ = co$(Asset.class);
        final Asset assetByUser1 = co$.new_().setDesc("new desc");
        
        // the first attempt to save asset should fail
        try {
            co$.saveWithError(assetByUser1);
            fail("Should have failed the first saving attempt.");
        } catch (final Result ex) {
            assertEquals(ERR_FAILED_SAVE, ex.getMessage());
        }
        
        assertFalse(assetByUser1.isPersisted());
        assertFalse(co$.entityExists(assetByUser1));
        assertEquals(DEFAULT_ASSET_NUMBER, assetByUser1.getNumber());
        
        // another user saves some asset concurrently
        final Asset assetSavedByUser2 = co$.save(co$.new_().setDesc("another new desc"));
        assertTrue(assetSavedByUser2.isPersisted());
        assertTrue(co$.entityExists(assetSavedByUser2));
        assertEquals("1", assetSavedByUser2.getNumber());

        // another attempt by User1 to save the failed asset
        final Asset savedAssetByUser1 = co$.save(assetByUser1);
        assertTrue(savedAssetByUser1.isPersisted());
        assertTrue(co$.entityExists(savedAssetByUser1));
        assertEquals("2", savedAssetByUser1.getNumber());
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
