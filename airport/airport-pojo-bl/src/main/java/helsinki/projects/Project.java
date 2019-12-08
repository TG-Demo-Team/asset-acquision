package helsinki.projects;

import java.util.Date;

import ua.com.fielden.platform.entity.AbstractPersistentEntity;
import ua.com.fielden.platform.entity.DynamicEntityKey;
import ua.com.fielden.platform.entity.annotation.CompanionObject;
import ua.com.fielden.platform.entity.annotation.CompositeKeyMember;
import ua.com.fielden.platform.entity.annotation.DateOnly;
import ua.com.fielden.platform.entity.annotation.Dependent;
import ua.com.fielden.platform.entity.annotation.DescRequired;
import ua.com.fielden.platform.entity.annotation.DescTitle;
import ua.com.fielden.platform.entity.annotation.DisplayDescription;
import ua.com.fielden.platform.entity.annotation.IsProperty;
import ua.com.fielden.platform.entity.annotation.KeyTitle;
import ua.com.fielden.platform.entity.annotation.KeyType;
import ua.com.fielden.platform.entity.annotation.MapEntityTo;
import ua.com.fielden.platform.entity.annotation.MapTo;
import ua.com.fielden.platform.entity.annotation.Observable;
import ua.com.fielden.platform.entity.annotation.Required;
import ua.com.fielden.platform.entity.annotation.Title;
import ua.com.fielden.platform.entity.validation.annotation.GeProperty;
import ua.com.fielden.platform.entity.validation.annotation.LeProperty;
import ua.com.fielden.platform.reflection.TitlesDescsGetter;
import ua.com.fielden.platform.utils.Pair;

/**
 * Master entity object.
 *
 * @author Developers
 *
 */
@KeyType(DynamicEntityKey.class)
@KeyTitle("Project")
@CompanionObject(IProject.class)
@MapEntityTo
@DescTitle("Description")
@DisplayDescription
@DescRequired
public class Project extends AbstractPersistentEntity<DynamicEntityKey> {

    private static final Pair<String, String> entityTitleAndDesc = TitlesDescsGetter.getEntityTitleAndDesc(Project.class);
    public static final String ENTITY_TITLE = entityTitleAndDesc.getKey();
    public static final String ENTITY_DESC = entityTitleAndDesc.getValue();
    
    @IsProperty
    @MapTo
    @Title(value = "Name", desc = "The project name")
    @CompositeKeyMember(1)
    private String name;

    @IsProperty
    @MapTo
    @Required
    @DateOnly
    @Dependent("finishDate")
    @Title(value = "Start Date", desc = "This is when this project was started")
    private Date startDate;

    @IsProperty
    @MapTo
    @DateOnly
    @Dependent("startDate")
    @Title(value = "Finish Date", desc = "This is when this project was completed")
    private Date finishDate;

    @Observable
    @LeProperty("finishDate")
    public Project setStartDate(final Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public Date getStartDate() {
        return startDate;
    }

    @Observable
    @GeProperty("startDate")
    public Project setFinishDate(final Date finishDate) {
        this.finishDate = finishDate;
        return this;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    @Observable
    public Project setName(final String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

}
