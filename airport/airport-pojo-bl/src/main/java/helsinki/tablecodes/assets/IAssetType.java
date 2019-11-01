package helsinki.tablecodes.assets;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.utils.EntityUtils;
import ua.com.fielden.platform.dao.IEntityDao;

/**
 * Companion object for entity {@link AssetType}.
 *
 * @author Developers
 *
 */
public interface IAssetType extends IEntityDao<AssetType> {

    static final IFetchProvider<AssetType> FETCH_PROVIDER = EntityUtils.fetch(AssetType.class)
            .with("name", "desc", "assetClass");

}
