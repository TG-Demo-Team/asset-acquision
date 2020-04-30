package helsinki.tablecodes.assets;

import com.google.inject.Inject;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.entity.annotation.EntityType;
/**
 * DAO implementation for companion object {@link IAssetClassAudit}.
 *
 * @author Developers
 *
 */
@EntityType(AssetClassAudit.class)
public class AssetClassAuditDao extends CommonEntityDao<AssetClassAudit> implements IAssetClassAudit {

    @Inject
    public AssetClassAuditDao(final IFilter filter) {
        super(filter);
    }

    @Override
    protected IFetchProvider<AssetClassAudit> createFetchProvider() {
        return FETCH_PROVIDER;
    }
}
