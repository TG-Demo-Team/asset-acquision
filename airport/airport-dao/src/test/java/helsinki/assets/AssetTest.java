package helsinki.assets;

import static helsinki.assets.AssetDao.ERR_FAILED_SAVE;
import static helsinki.assets.IAsset.DEFAULT_ASSET_NUMBER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
    public void can_find_assets_by_fin_det_information() {
        final Asset asset1 = save(new_(Asset.class).setDesc("a demo asset 1"));
        final Asset asset2 = save(new_(Asset.class).setDesc("a demo asset 2"));
        final Asset asset3 = save(new_(Asset.class).setDesc("a demo asset 3"));
        
        final AssetFinDet finDet1 = co$(AssetFinDet.class).findById(asset1.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel());
        save(finDet1.setInitCost(Money.of("120.00")).setAcquireDate(date("2019-12-07 00:00:00")));
        final AssetFinDet finDet2 = co$(AssetFinDet.class).findById(asset2.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel());
        save(finDet2.setInitCost(Money.of("100.00")).setAcquireDate(date("2019-11-01 00:00:00")));
        final AssetFinDet finDet3 = co$(AssetFinDet.class).findById(asset3.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel());
        save(finDet3.setInitCost(Money.of("10.00")).setAcquireDate(date("2018-11-01 00:00:00")));
        
        final QueryExecutionModel<Asset, EntityResultQueryModel<Asset>> qFindWithLessOrEq100 = 
                from(select(Asset.class).where().prop("finDet.initCost").le().val(Money.of("100.00")).model())
                .with(fetch(Asset.class).with("number", "desc", "finDet.initCost", "finDet.acquireDate").fetchModel())
                .with(orderBy().prop("finDet.initCost").asc().model()).model();
        
        final List<Asset> assets = co(Asset.class).getAllEntities(qFindWithLessOrEq100);
        assertEquals(2, assets.size());
        
        assertEquals(Money.of("10.00"), assets.get(0).getFinDet().getInitCost());
        assertEquals("3", assets.get(0).getNumber());
        assertEquals(Money.of("100.00"), assets.get(1).getFinDet().getInitCost());
        assertEquals("2", assets.get(1).getNumber());

        final QueryExecutionModel<Asset, EntityResultQueryModel<Asset>> qFindWithGreater100 = 
                from(select(Asset.class).where().prop("finDet.initCost").gt().val(Money.of("100.00")).model())
                .with(fetch(Asset.class).with("number", "desc", "finDet.initCost", "finDet.acquireDate").fetchModel())
                .with(orderBy().prop("finDet.initCost").asc().model()).model();
        
        final List<Asset> assetsMoreExpensiveThan100 = co(Asset.class).getAllEntities(qFindWithGreater100);
        assertEquals(1, assetsMoreExpensiveThan100.size());
        
        assertEquals(Money.of("120.00"), assetsMoreExpensiveThan100.get(0).getFinDet().getInitCost());
        assertEquals("1", assetsMoreExpensiveThan100.get(0).getNumber());
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
