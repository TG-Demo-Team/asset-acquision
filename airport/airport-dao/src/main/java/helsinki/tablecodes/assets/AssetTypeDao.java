package helsinki.tablecodes.assets;

import com.google.inject.Inject;

import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.entity.annotation.EntityType;
import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.entity.query.IFilter;
/**
 * DAO implementation for companion object {@link IAssetType}.
 *
 * @author Developers
 *
 */
@EntityType(AssetType.class)
public class AssetTypeDao extends CommonEntityDao<AssetType> implements IAssetType {

    @Inject
    public AssetTypeDao(final IFilter filter) {
        super(filter);
    }

    @Override
    protected IFetchProvider<AssetType> createFetchProvider() {
        return FETCH_PROVIDER;
    }
}
