package helsinki.assets;

import java.util.Date;

import helsinki.assets.validators.AssetFinDetAcquireDateWithinProjectPeriodValidator;
import helsinki.projects.Project;
import ua.com.fielden.platform.entity.AbstractPersistentEntity;
import ua.com.fielden.platform.entity.annotation.CompanionObject;
import ua.com.fielden.platform.entity.annotation.DateOnly;
import ua.com.fielden.platform.entity.annotation.Dependent;
import ua.com.fielden.platform.entity.annotation.IsProperty;
import ua.com.fielden.platform.entity.annotation.KeyTitle;
import ua.com.fielden.platform.entity.annotation.KeyType;
import ua.com.fielden.platform.entity.annotation.MapEntityTo;
import ua.com.fielden.platform.entity.annotation.MapTo;
import ua.com.fielden.platform.entity.annotation.Observable;
import ua.com.fielden.platform.entity.annotation.SkipEntityExistsValidation;
import ua.com.fielden.platform.entity.annotation.Title;
import ua.com.fielden.platform.entity.annotation.mutator.BeforeChange;
import ua.com.fielden.platform.entity.annotation.mutator.Handler;
import ua.com.fielden.platform.reflection.TitlesDescsGetter;
import ua.com.fielden.platform.types.Money;
import ua.com.fielden.platform.utils.Pair;

/**
 * An entity to capture financial details for an asset.
 *
 * @author Helsinki Team
 *
 */
@KeyType(Asset.class)
@KeyTitle("Asset")
@CompanionObject(IAssetFinDet.class)
@MapEntityTo
public class AssetFinDet extends AbstractPersistentEntity<Asset> {

    private static final Pair<String, String> entityTitleAndDesc = TitlesDescsGetter.getEntityTitleAndDesc(AssetFinDet.class);
    public static final String ENTITY_TITLE = entityTitleAndDesc.getKey();
    public static final String ENTITY_DESC = entityTitleAndDesc.getValue();

    @IsProperty
    @MapTo
    @SkipEntityExistsValidation(skipActiveOnly = true)
    private Asset key;

    @IsProperty
    @MapTo
    @Title(value = "Init Cost", desc = "Initial asset cost")
    private Money initCost;

    @IsProperty
    @DateOnly
    @MapTo
    @Title(value = "Acquire Date", desc = "The date when asset was made or purchased")
    @BeforeChange(@Handler(AssetFinDetAcquireDateWithinProjectPeriodValidator.class))
    private Date acquireDate;

    @IsProperty
    @MapTo
    @Dependent("acquireDate")
    @Title(value = "Project", desc = "Capex project for the acquisition of this asset")
    private Project project;

    @Observable
    public AssetFinDet setProject(final Project project) {
        this.project = project;
        return this;
    }

    public Project getProject() {
        return project;
    }

    @Override
    @Observable
    public AssetFinDet setKey(final Asset key) {
        this.key = key;
        return this;
    }
    
    @Override
    public Asset getKey() {
        return key;
    }

    @Observable
    public AssetFinDet setAcquireDate(final Date acquireDate) {
        this.acquireDate = acquireDate;
        return this;
    }

    public Date getAcquireDate() {
        return acquireDate;
    }

    @Observable
    public AssetFinDet setInitCost(final Money initCost) {
        this.initCost = initCost;
        return this;
    }

    public Money getInitCost() {
        return initCost;
    }

}
