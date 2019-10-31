package helsinki.tablecodes.assets;

import com.google.inject.Inject;

import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.entity.annotation.EntityType;
import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.entity.query.IFilter;
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
        return FETCH_PROVIDER;
    }
}
