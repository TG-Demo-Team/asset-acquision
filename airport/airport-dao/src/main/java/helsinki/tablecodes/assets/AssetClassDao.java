package helsinki.tablecodes.assets;

import com.google.inject.Inject;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.entity.annotation.EntityType;
/**
 * DAO implementation for companion object {@link IAssetClass}.
 *
 * @author Developers
 *
 */
@EntityType(AssetClass.class)
public class AssetClassDao extends CommonEntityDao<AssetClass> implements IAssetClass {

    @Inject
    public AssetClassDao(final IFilter filter) {
        super(filter);
    }

    @Override
    protected IFetchProvider<AssetClass> createFetchProvider() {
        // TODO: uncomment the following line and specify the properties, which are required for the UI in IAssetClass.FETCH_PROVIDER. Then remove the line after.
        // return FETCH_PROVIDER;
        throw new UnsupportedOperationException("Please specify the properties, which are required for the UI in IAssetClass.FETCH_PROVIDER");
    }
}
