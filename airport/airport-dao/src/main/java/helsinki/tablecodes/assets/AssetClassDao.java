package helsinki.tablecodes.assets;

import com.google.inject.Inject;

import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.dao.annotations.SessionRequired;
import ua.com.fielden.platform.entity.annotation.EntityType;
import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.error.Result;

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

    @SessionRequired
    @Override
    public AssetClass save(final AssetClass ac) {
        ac.isValid().ifFailure(Result::throwRuntime);

        final IAssetClassAudit coAssetClassAudit = co$(AssetClassAudit.class);
        coAssetClassAudit.save(ac.copyTo(coAssetClassAudit.new_()));

        return super.save(ac);
    }

    @Override
    protected IFetchProvider<AssetClass> createFetchProvider() {
        return FETCH_PROVIDER;
    }
}
