package helsinki.webapp.config.projects;

import static java.lang.String.format;
import static helsinki.common.StandardScrollingConfigs.standardStandaloneScrollingConfig;

import java.util.Optional;

import com.google.inject.Injector;

import helsinki.projects.Project;
import helsinki.common.LayoutComposer;
import helsinki.common.StandardActions;

import ua.com.fielden.platform.web.interfaces.ILayout.Device;
import ua.com.fielden.platform.web.action.CentreConfigurationWebUiConfig.CentreConfigActions;
import ua.com.fielden.platform.web.centre.api.EntityCentreConfig;
import ua.com.fielden.platform.web.centre.api.actions.EntityActionConfig;
import ua.com.fielden.platform.web.centre.api.impl.EntityCentreBuilder;
import ua.com.fielden.platform.web.view.master.api.actions.MasterActions;
import ua.com.fielden.platform.web.view.master.api.impl.SimpleMasterBuilder;
import ua.com.fielden.platform.web.view.master.api.IMaster;
import ua.com.fielden.platform.web.app.config.IWebUiBuilder;
import helsinki.main.menu.projects.MiProject;
import ua.com.fielden.platform.web.centre.EntityCentre;
import ua.com.fielden.platform.web.view.master.EntityMaster;
import static ua.com.fielden.platform.web.PrefDim.mkDim;
import ua.com.fielden.platform.web.PrefDim.Unit;
/**
 * {@link Project} Web UI configuration.
 *
 * @author Developers
 *
 */
public class ProjectWebUiConfig {

    public final EntityCentre<Project> centre;
    public final EntityMaster<Project> master;

    public static ProjectWebUiConfig register(final Injector injector, final IWebUiBuilder builder) {
        return new ProjectWebUiConfig(injector, builder);
    }

    private ProjectWebUiConfig(final Injector injector, final IWebUiBuilder builder) {
        centre = createCentre(injector, builder);
        builder.register(centre);
        master = createMaster(injector);
        builder.register(master);
    }

    /**
     * Creates entity centre for {@link Project}.
     *
     * @param injector
     * @return created entity centre
     */
    private EntityCentre<Project> createCentre(final Injector injector, final IWebUiBuilder builder) {
        final String layout = LayoutComposer.mkGridForCentre(2, 2);

        final EntityActionConfig standardNewAction = StandardActions.NEW_ACTION.mkAction(Project.class);
        final EntityActionConfig standardExportAction = StandardActions.EXPORT_ACTION.mkAction(Project.class);
        final EntityActionConfig standardEditAction = StandardActions.EDIT_ACTION.mkAction(Project.class);
        final EntityActionConfig standardSortAction = CentreConfigActions.CUSTOMISE_COLUMNS_ACTION.mkAction();
        builder.registerOpenMasterAction(Project.class, standardEditAction);

        final EntityCentreConfig<Project> ecc = EntityCentreBuilder.centreFor(Project.class)
                //.runAutomatically()
                .addFrontAction(standardNewAction)
                .addTopAction(standardNewAction).also()
                .addTopAction(standardSortAction).also()
                .addTopAction(standardExportAction)
                .addCrit("this").asMulti().autocompleter(Project.class).also()
                .addCrit("desc").asMulti().text().also()
                .addCrit("startDate").asRange().date().also()
                .addCrit("finishDate").asRange().date()
                .setLayoutFor(Device.DESKTOP, Optional.empty(), layout)
                .setLayoutFor(Device.TABLET, Optional.empty(), layout)
                .setLayoutFor(Device.MOBILE, Optional.empty(), layout)
                .withScrollingConfig(standardStandaloneScrollingConfig(0))
                .addProp("this").order(1).asc().width(150)
                    .withSummary("total_count_", "COUNT(SELF)", format("Count:The total number of matching %ss.", Project.ENTITY_TITLE))
                    .withAction(standardEditAction).also()
                .addProp("desc").minWidth(200).also()
                .addProp("startDate").width(150).also()
                .addProp("finishDate").width(150)
                //.addProp("prop").minWidth(100).withActionSupplier(builder.getOpenMasterAction(Entity.class)).also()
                .addPrimaryAction(standardEditAction)
                .build();

        return new EntityCentre<>(MiProject.class, MiProject.class.getSimpleName(), ecc, injector, null);
    }

    /**
     * Creates entity master for {@link Project}.
     *
     * @param injector
     * @return created entity master
     */
    private EntityMaster<Project> createMaster(final Injector injector) {
        final String layout = LayoutComposer.mkVarGridForMasterFitWidth(1, 1, 2);

        final IMaster<Project> masterConfig = new SimpleMasterBuilder<Project>().forEntity(Project.class)
                .addProp("name").asSinglelineText().also()
                .addProp("desc").asMultilineText().also()
                .addProp("startDate").asDatePicker().also()
                .addProp("finishDate").asDatePicker().also()
                .addAction(MasterActions.REFRESH).shortDesc("Cancel").longDesc("Cancel action")
                .addAction(MasterActions.SAVE)
                .setActionBarLayoutFor(Device.DESKTOP, Optional.empty(), LayoutComposer.mkActionLayoutForMaster())
                .setLayoutFor(Device.DESKTOP, Optional.empty(), layout)
                .setLayoutFor(Device.TABLET, Optional.empty(), layout)
                .setLayoutFor(Device.MOBILE, Optional.empty(), layout)
                .withDimensions(mkDim(LayoutComposer.SIMPLE_ONE_COLUMN_MASTER_DIM_WIDTH, 480, Unit.PX))
                .done();

        return new EntityMaster<>(Project.class, masterConfig, injector);
    }
}