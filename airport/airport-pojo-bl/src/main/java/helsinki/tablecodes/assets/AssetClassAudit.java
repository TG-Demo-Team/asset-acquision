package helsinki.tablecodes.assets;

import java.util.Date;

import ua.com.fielden.platform.entity.AbstractPersistentEntity;
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
import ua.com.fielden.platform.entity.annotation.Readonly;
import ua.com.fielden.platform.entity.annotation.Title;
import ua.com.fielden.platform.error.Result;
import ua.com.fielden.platform.reflection.TitlesDescsGetter;
import ua.com.fielden.platform.utils.Pair;

/**
 * An audit entity for {@link AssetClass}.
 *
 * @author Developers
 *
 */
@KeyType(DynamicEntityKey.class)
@KeyTitle("Key")
@CompanionObject(IAssetClassAudit.class)
@MapEntityTo
@DescTitle("Description")
@DisplayDescription
@DescRequired
public class AssetClassAudit extends AssetClass {

    private static final Pair<String, String> entityTitleAndDesc = TitlesDescsGetter.getEntityTitleAndDesc(AssetClassAudit.class);
    public static final String ENTITY_TITLE = entityTitleAndDesc.getKey();
    public static final String ENTITY_DESC = entityTitleAndDesc.getValue();

    @IsProperty(assignBeforeSave = true)
    @MapTo
    @Readonly
    @CompositeKeyMember(2)
    @Title("Audit Date")
    private Date auditDate;

    @Observable
    protected AssetClassAudit setAuditDate(final Date creationTime) {
        this.auditDate = creationTime;
        return this;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    @Override
    public Result isEditable() {
        return this.isPersisted() ? Result.failure("Persisted audit instances are not editable.") : Result.successful("ok");
    }
}
