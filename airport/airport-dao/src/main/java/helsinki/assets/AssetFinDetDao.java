package helsinki.assets;

import com.google.inject.Inject;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.entity.annotation.EntityType;
/**
 * DAO implementation for companion object {@link IAssetFinDet}.
 *
 * @author Developers
 *
 */
@EntityType(AssetFinDet.class)
public class AssetFinDetDao extends CommonEntityDao<AssetFinDet> implements IAssetFinDet {

    @Inject
    public AssetFinDetDao(final IFilter filter) {
        super(filter);
    }

    @Override
    protected IFetchProvider<AssetFinDet> createFetchProvider() {
        // TODO: uncomment the following line and specify the properties, which are required for the UI in IAssetFinDet.FETCH_PROVIDER. Then remove the line after.
        // return FETCH_PROVIDER;
        throw new UnsupportedOperationException("Please specify the properties, which are required for the UI in IAssetFinDet.FETCH_PROVIDER");
    }
}
