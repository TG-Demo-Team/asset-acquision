package helsinki.webapp.config.assets;

import static helsinki.common.StandardScrollingConfigs.standardStandaloneScrollingConfig;
import static java.lang.String.format;
import static ua.com.fielden.platform.web.PrefDim.mkDim;

import java.util.Optional;

import com.google.inject.Injector;

import helsinki.assets.Asset;
import helsinki.assets.AssetFinDet;
import helsinki.common.LayoutComposer;
import helsinki.common.StandardActions;
import helsinki.main.menu.assets.MiAssetFinDet;
import ua.com.fielden.platform.web.PrefDim.Unit;
import ua.com.fielden.platform.web.action.CentreConfigurationWebUiConfig.CentreConfigActions;
import ua.com.fielden.platform.web.app.config.IWebUiBuilder;
import ua.com.fielden.platform.web.centre.EntityCentre;
import ua.com.fielden.platform.web.centre.api.EntityCentreConfig;
import ua.com.fielden.platform.web.centre.api.actions.EntityActionConfig;
import ua.com.fielden.platform.web.centre.api.impl.EntityCentreBuilder;
import ua.com.fielden.platform.web.interfaces.ILayout.Device;
import ua.com.fielden.platform.web.view.master.EntityMaster;
import ua.com.fielden.platform.web.view.master.api.IMaster;
import ua.com.fielden.platform.web.view.master.api.actions.MasterActions;
import ua.com.fielden.platform.web.view.master.api.impl.SimpleMasterBuilder;
/**
 * {@link AssetFinDet} Web UI configuration.
 *
 * @author Developers
 *
 */
public class AssetFinDetWebUiConfig {

    public final EntityCentre<AssetFinDet> centre;
    public final EntityMaster<AssetFinDet> master;

    public static AssetFinDetWebUiConfig register(final Injector injector, final IWebUiBuilder builder) {
        return new AssetFinDetWebUiConfig(injector, builder);
    }

    private AssetFinDetWebUiConfig(final Injector injector, final IWebUiBuilder builder) {
        centre = createCentre(injector, builder);
        builder.register(centre);
        master = createMaster(injector);
        builder.register(master);
    }

    /**
     * Creates entity centre for {@link AssetFinDet}.
     *
     * @param injector
     * @return created entity centre
     */
    private EntityCentre<AssetFinDet> createCentre(final Injector injector, final IWebUiBuilder builder) {
        final String layout = LayoutComposer.mkGridForCentre(3, 1);

        final EntityActionConfig standardNewAction = StandardActions.NEW_ACTION.mkAction(AssetFinDet.class);
        final EntityActionConfig standardExportAction = StandardActions.EXPORT_ACTION.mkAction(AssetFinDet.class);
        final EntityActionConfig standardEditAction = StandardActions.EDIT_ACTION.mkAction(AssetFinDet.class);
        final EntityActionConfig standardSortAction = CentreConfigActions.CUSTOMISE_COLUMNS_ACTION.mkAction();
        builder.registerOpenMasterAction(AssetFinDet.class, standardEditAction);

        final EntityCentreConfig<AssetFinDet> ecc = EntityCentreBuilder.centreFor(AssetFinDet.class)
                //.runAutomatically()
                .addFrontAction(standardNewAction)
                .addTopAction(standardNewAction).also()
                .addTopAction(standardSortAction).also()
                .addTopAction(standardExportAction)
                .addCrit("key").asMulti().autocompleter(Asset.class).also()
                .addCrit("initCost").asRange().decimal().also()
                .addCrit("acquireDate").asRange().date()
                .setLayoutFor(Device.DESKTOP, Optional.empty(), layout)
                .setLayoutFor(Device.TABLET, Optional.empty(), layout)
                .setLayoutFor(Device.MOBILE, Optional.empty(), layout)
                .withScrollingConfig(standardStandaloneScrollingConfig(0))
                .addProp("key").order(1).asc().width(100)
                    .withSummary("total_count_", "COUNT(SELF)", format("Count:The total number of matching %ss.", AssetFinDet.ENTITY_TITLE))
                    .withActionSupplier(builder.getOpenMasterAction(Asset.class)).also()
                .addProp("key.desc").minWidth(200).also()
                .addEditableProp("initCost").width(150)
                    .withSummary("total_init_cost_", "SUM(initCost)", "Total:Total initial cost for selected assets.").also()
                .addEditableProp("acquireDate").width(150)
                .addPrimaryAction(standardEditAction)
                .build();

        return new EntityCentre<>(MiAssetFinDet.class, MiAssetFinDet.class.getSimpleName(), ecc, injector, null);
    }

    /**
     * Creates entity master for {@link AssetFinDet}.
     *
     * @param injector
     * @return created entity master
     */
    private EntityMaster<AssetFinDet> createMaster(final Injector injector) {
        final String layout = LayoutComposer.mkVarGridForMasterFitWidth(1, 1, 2);

        final IMaster<AssetFinDet> masterConfig = new SimpleMasterBuilder<AssetFinDet>().forEntity(AssetFinDet.class)
                .addProp("key").asAutocompleter().also()
                .addProp("project").asAutocompleter().also()
                .addProp("initCost").asMoney().also()
                .addProp("acquireDate").asDatePicker().also()
                .addAction(MasterActions.REFRESH).shortDesc("Cancel").longDesc("Cancel action")
                .addAction(MasterActions.SAVE)
                .setActionBarLayoutFor(Device.DESKTOP, Optional.empty(), LayoutComposer.mkActionLayoutForMaster())
                .setLayoutFor(Device.DESKTOP, Optional.empty(), layout)
                .setLayoutFor(Device.TABLET, Optional.empty(), layout)
                .setLayoutFor(Device.MOBILE, Optional.empty(), layout)
                .withDimensions(mkDim(LayoutComposer.SIMPLE_ONE_COLUMN_MASTER_DIM_WIDTH, 480, Unit.PX))
                .done();

        return new EntityMaster<>(AssetFinDet.class, masterConfig, injector);
    }
}