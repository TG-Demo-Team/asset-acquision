package helsinki.tablecodes.assets.ui_actions;

import com.google.inject.Inject;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.dao.AbstractOpenCompoundMasterDao;
import ua.com.fielden.platform.dao.IEntityAggregatesOperations;
import helsinki.tablecodes.assets.AssetType;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.entity.annotation.EntityType;
/**
 * DAO implementation for companion object {@link IOpenAssetClassMasterAction}.
 *
 * @author Developers
 *
 */
@EntityType(OpenAssetClassMasterAction.class)
public class OpenAssetClassMasterActionDao extends AbstractOpenCompoundMasterDao<OpenAssetClassMasterAction> implements IOpenAssetClassMasterAction {

    @Inject
    public OpenAssetClassMasterActionDao(final IFilter filter, final IEntityAggregatesOperations coAggregates) {
        super(filter, coAggregates);
        addViewBinding(OpenAssetClassMasterAction.ASSETTYPES, AssetType.class, "assetClass");
    }

    @Override
    protected IFetchProvider<OpenAssetClassMasterAction> createFetchProvider() {
        return FETCH_PROVIDER;
    }

}
