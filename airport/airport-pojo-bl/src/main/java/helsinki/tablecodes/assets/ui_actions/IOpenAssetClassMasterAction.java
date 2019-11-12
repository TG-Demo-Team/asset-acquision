package helsinki.tablecodes.assets.ui_actions;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.utils.EntityUtils;
import ua.com.fielden.platform.dao.IEntityDao;

/**
 * Companion object for entity {@link OpenAssetClassMasterAction}.
 *
 * @author Developers
 *
 */
public interface IOpenAssetClassMasterAction extends IEntityDao<OpenAssetClassMasterAction> {

    static final IFetchProvider<OpenAssetClassMasterAction> FETCH_PROVIDER = EntityUtils.fetch(OpenAssetClassMasterAction.class).with(
        // key is needed to be correctly autopopulated by newly saved compound master entity (ID-based restoration of entity-typed key)
        "key");

}
