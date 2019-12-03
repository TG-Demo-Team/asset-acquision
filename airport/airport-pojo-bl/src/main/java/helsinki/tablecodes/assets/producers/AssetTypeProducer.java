package helsinki.tablecodes.assets.producers;

import com.google.inject.Inject;

import helsinki.tablecodes.assets.AssetClass;
import helsinki.tablecodes.assets.AssetType;
import ua.com.fielden.platform.entity.DefaultEntityProducerWithContext;
import ua.com.fielden.platform.entity.EntityNewAction;
import ua.com.fielden.platform.entity.factory.EntityFactory;
import ua.com.fielden.platform.entity.factory.ICompanionObjectFinder;
import ua.com.fielden.platform.error.Result;
/**
 * A producer for new instances of entity {@link AssetType}.
 *
 * @author Developers
 *
 */
public class AssetTypeProducer extends DefaultEntityProducerWithContext<AssetType> {

    @Inject
    public AssetTypeProducer(final EntityFactory factory, final ICompanionObjectFinder coFinder) {
        super(factory, AssetType.class, coFinder);
    }

    @Override
    protected AssetType provideDefaultValuesForStandardNew(final AssetType entityIn, final EntityNewAction masterEntity) {
        final AssetType entityOut = super.provideDefaultValuesForStandardNew(entityIn, masterEntity);
        // This producer can be invoked from two places:
        // 1. Standalone centre
        // 2. Centre embedded in AssetClass Master
        // In the second case we want to default the assetClass and make it read-only
        if (ofMasterEntity().keyOfMasterEntityInstanceOf(AssetClass.class)) {
            final AssetClass shallowAssetClass = ofMasterEntity().keyOfMasterEntity(AssetClass.class);
            // shallowAssetClass has been fetched in OpenAssetClassMasterActionProducer with key and desc only
            // It needs to be re-fetched here using a slightly deeper fetch model, as appropriate for CocEntry
            entityOut.setAssetClass(refetch(shallowAssetClass, "assetClass"));
            entityOut.getProperty("assetClass").validationResult().ifFailure(Result::throwRuntime);
            entityOut.getProperty("assetClass").setEditable(false);
        }
        return entityOut;
    }
}
