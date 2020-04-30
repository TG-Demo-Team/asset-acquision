package helsinki.tablecodes.assets;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.utils.EntityUtils;
import ua.com.fielden.platform.dao.IEntityDao;

/**
 * Companion object for entity {@link AssetClassAudit}.
 *
 * @author Developers
 *
 */
public interface IAssetClassAudit extends IEntityDao<AssetClassAudit> {

    static final IFetchProvider<AssetClassAudit> FETCH_PROVIDER = EntityUtils.fetch(AssetClassAudit.class)
            .with("key", "desc");


}
