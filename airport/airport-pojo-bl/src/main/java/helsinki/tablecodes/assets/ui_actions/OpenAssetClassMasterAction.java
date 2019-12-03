package helsinki.tablecodes.assets.ui_actions;

import helsinki.tablecodes.assets.AssetClass;
import helsinki.tablecodes.assets.AssetType;
import ua.com.fielden.platform.entity.AbstractFunctionalEntityToOpenCompoundMaster;
import ua.com.fielden.platform.entity.annotation.KeyType;
import ua.com.fielden.platform.entity.annotation.EntityTitle;
import ua.com.fielden.platform.entity.annotation.CompanionObject;
import ua.com.fielden.platform.reflection.TitlesDescsGetter;
import ua.com.fielden.platform.utils.Pair;

/**
 * Open Master Action entity object.
 *
 * @author Developers
 *
 */
@KeyType(AssetClass.class)
@CompanionObject(IOpenAssetClassMasterAction.class)
@EntityTitle("Asset Class Master")
public class OpenAssetClassMasterAction extends AbstractFunctionalEntityToOpenCompoundMaster<AssetClass> {

    private static final Pair<String, String> entityTitleAndDesc = TitlesDescsGetter.getEntityTitleAndDesc(OpenAssetClassMasterAction.class);
    public static final String ENTITY_TITLE = entityTitleAndDesc.getKey();
    public static final String ENTITY_DESC = entityTitleAndDesc.getValue();

    public static final String MAIN = "Main";
    public static final String ASSETTYPES = AssetType.ENTITY_TITLE + "s"; // Please adjust manually if the plural form is not standard
}
