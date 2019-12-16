package helsinki.projects.validators;

import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.select;
import static ua.com.fielden.platform.error.Result.failure;
import static ua.com.fielden.platform.error.Result.successful;

import java.lang.annotation.Annotation;
import java.util.Date;
import java.util.Set;

import helsinki.assets.AssetFinDet;
import helsinki.projects.Project;
import ua.com.fielden.platform.entity.meta.MetaProperty;
import ua.com.fielden.platform.entity.meta.impl.AbstractBeforeChangeEventHandler;
import ua.com.fielden.platform.entity.query.fluent.EntityQueryProgressiveInterfaces.IWhere0;
import ua.com.fielden.platform.entity.query.model.EntityResultQueryModel;
import ua.com.fielden.platform.error.Result;

public class ProjectStartAndFinishDatesValidator extends AbstractBeforeChangeEventHandler<Date> {
    public static final String ERR_OUTSIDE_NEW_PERIOD_DUE_TO_START_DATE = "There are assets that become ouside of the project period with this new start date.";
    public static final String ERR_OUTSIDE_NEW_PERIOD_DUE_TO_FINISH_DATE = "There are assets that become ouside of the project period with this new finish date.";

    @Override
    public Result handle(final MetaProperty<Date> property, final Date newValue, final Set<Annotation> mutatorAnnotations) {
        final Project project = property.getEntity();
        
        if (!project.isPersisted()) {
            return successful(newValue);
        }

        final String err = "startDate".equals(property.getName()) ? ERR_OUTSIDE_NEW_PERIOD_DUE_TO_START_DATE : ERR_OUTSIDE_NEW_PERIOD_DUE_TO_FINISH_DATE;
        final Date startDate = "startDate".equals(property.getName()) ? newValue : project.getStartDate();
        final Date finishDate = "finishDate".equals(property.getName()) ? newValue : project.getFinishDate();
        
        final IWhere0<AssetFinDet> partialQuery = select(AssetFinDet.class).where()
                .prop("project").eq().val(project).and()
                .prop("acquireDate").isNotNull().and();
        final EntityResultQueryModel<AssetFinDet> query;
        if (finishDate == null) {
            query = partialQuery.prop("acquireDate").lt().val(startDate).model();
        } else {
            query = partialQuery
                    .begin()
                        .prop("acquireDate").lt().val(startDate).or()
                        .prop("acquireDate").gt().val(finishDate)
                    .end().model();
        }
        
        return co(AssetFinDet.class).exists(query) ? failure(err) : successful(newValue);
    }

}
