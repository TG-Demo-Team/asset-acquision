package helsinki.projects;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.utils.EntityUtils;
import ua.com.fielden.platform.dao.IEntityDao;

/**
 * Companion object for entity {@link Project}.
 *
 * @author Developers
 *
 */
public interface IProject extends IEntityDao<Project> {

    static final IFetchProvider<Project> FETCH_PROVIDER = EntityUtils.fetch(Project.class)
            .with("name", "desc", "startDate", "finishDate");

}
