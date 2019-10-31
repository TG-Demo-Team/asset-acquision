package helsinki.tablecodes.assets;

import helsinki.tablecodes.validators.LongerThan2Validator;
import helsinki.tablecodes.validators.NoSpacesValidator;
import ua.com.fielden.platform.entity.ActivatableAbstractEntity;
import ua.com.fielden.platform.entity.DynamicEntityKey;
import ua.com.fielden.platform.entity.annotation.CompanionObject;
import ua.com.fielden.platform.entity.annotation.CompositeKeyMember;
import ua.com.fielden.platform.entity.annotation.DescRequired;
import ua.com.fielden.platform.entity.annotation.DescTitle;
import ua.com.fielden.platform.entity.annotation.DisplayDescription;
import ua.com.fielden.platform.entity.annotation.IsProperty;
import ua.com.fielden.platform.entity.annotation.KeyTitle;
import ua.com.fielden.platform.entity.annotation.KeyType;
import ua.com.fielden.platform.entity.annotation.MapEntityTo;
import ua.com.fielden.platform.entity.annotation.MapTo;
import ua.com.fielden.platform.entity.annotation.Observable;
import ua.com.fielden.platform.entity.annotation.Title;
import ua.com.fielden.platform.entity.annotation.UpperCase;
import ua.com.fielden.platform.entity.annotation.mutator.BeforeChange;
import ua.com.fielden.platform.entity.annotation.mutator.Handler;
import ua.com.fielden.platform.entity.validation.annotation.Final;
import ua.com.fielden.platform.reflection.TitlesDescsGetter;
import ua.com.fielden.platform.utils.Pair;

/**
 * Master entity object.
 *
 * @author Developers
 *
 */
@KeyType(DynamicEntityKey.class)
@CompanionObject(IAssetClass.class)
@MapEntityTo
@DescTitle("Asset class description")
@DisplayDescription
@DescRequired
@KeyTitle("Asset Class")
public class AssetClass extends ActivatableAbstractEntity<DynamicEntityKey> {

    private static final Pair<String, String> entityTitleAndDesc = TitlesDescsGetter.getEntityTitleAndDesc(AssetClass.class);
    public static final String ENTITY_TITLE = entityTitleAndDesc.getKey();
    public static final String ENTITY_DESC = entityTitleAndDesc.getValue();

    @IsProperty
    @MapTo
    @Title(value = "Name", desc = "Asset class name")
    @CompositeKeyMember(1)
    @BeforeChange({@Handler(NoSpacesValidator.class), @Handler(LongerThan2Validator.class)})
    @UpperCase
    private String name;

    @IsProperty
    @MapTo
    @Title(value = "Criticaly", desc = "Indicated how critical assets of this class are.")
    @Final
    private Integer criticality;

    @Observable
    public AssetClass setCriticality(final Integer criticality) {
        this.criticality = criticality;
        return this;
    }

    public Integer getCriticality() {
        return criticality;
    }
    
    @Observable
    public AssetClass setName(final String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    @Override
    @Observable
    public AssetClass setDesc(String desc) {
        super.setDesc(desc);
        return this;
    }
}
