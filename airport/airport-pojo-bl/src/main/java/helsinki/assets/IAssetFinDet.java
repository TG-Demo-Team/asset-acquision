package helsinki.assets;

import ua.com.fielden.platform.dao.IEntityDao;
import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.utils.EntityUtils;

/**
 * Companion object for entity {@link AssetFinDet}.
 *
 * @author Developers
 *
 */
public interface IAssetFinDet extends IEntityDao<AssetFinDet> {

    static final IFetchProvider<AssetFinDet> FETCH_PROVIDER = EntityUtils.fetch(AssetFinDet.class)
            .with("key", "initCost", "acquireDate", "project");

}
