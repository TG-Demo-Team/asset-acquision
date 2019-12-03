package helsinki.tablecodes.assets.master.menu.actions;

import helsinki.tablecodes.assets.AssetClass;
import ua.com.fielden.platform.entity.AbstractFunctionalEntityForCompoundMenuItem;
import ua.com.fielden.platform.entity.annotation.KeyType;
import ua.com.fielden.platform.entity.annotation.EntityTitle;
import ua.com.fielden.platform.entity.annotation.CompanionObject;
import ua.com.fielden.platform.reflection.TitlesDescsGetter;
import ua.com.fielden.platform.utils.Pair;

/**
 * Master entity object to model the main menu item of the compound master entity object.
 *
 * @author Developers
 *
 */
@KeyType(AssetClass.class)
@CompanionObject(IAssetClassMaster_OpenMain_MenuItem.class)
@EntityTitle("Asset Class Master Main Menu Item")
public class AssetClassMaster_OpenMain_MenuItem extends AbstractFunctionalEntityForCompoundMenuItem<AssetClass> {

    private static final Pair<String, String> entityTitleAndDesc = TitlesDescsGetter.getEntityTitleAndDesc(AssetClassMaster_OpenMain_MenuItem.class);
    public static final String ENTITY_TITLE = entityTitleAndDesc.getKey();
    public static final String ENTITY_DESC = entityTitleAndDesc.getValue();

}
