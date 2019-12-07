package helsinki.assets;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.utils.EntityUtils;
import ua.com.fielden.platform.dao.IEntityDao;

/**
 * Companion object for entity {@link AssetFinDet}.
 *
 * @author Developers
 *
 */
public interface IAssetFinDet extends IEntityDao<AssetFinDet> {

    static final IFetchProvider<AssetFinDet> FETCH_PROVIDER = EntityUtils.fetch(AssetFinDet.class).with(
        // TODO: uncomment the following line and specify the properties, which are required for the UI. Then remove the line after.
        // "key", "desc");
        "Please specify the properties, which are required for the UI");

}
