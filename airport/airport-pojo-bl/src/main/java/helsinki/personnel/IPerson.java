package helsinki.personnel;

import static java.lang.String.format;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.from;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.select;
import static ua.com.fielden.platform.error.Result.failure;
import static ua.com.fielden.platform.types.either.Either.left;
import static ua.com.fielden.platform.types.either.Either.right;

import ua.com.fielden.platform.dao.IEntityDao;
import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.entity.query.fluent.fetch;

import java.util.Optional;

import ua.com.fielden.platform.entity.query.model.EntityResultQueryModel;
import ua.com.fielden.platform.error.Result;
import ua.com.fielden.platform.security.user.User;
import ua.com.fielden.platform.types.either.Either;
import ua.com.fielden.platform.utils.EntityUtils;

/**
 * Companion contract for {@link Person}.
 *
 * @author Generated
 *
 */
public interface IPerson extends IEntityDao<Person> {
    static final IFetchProvider<Person> FETCH_PROVIDER = EntityUtils.fetch(Person.class)
            .with("key", "desc", "user", "title", "employeeNo", "phone", "mobile", "email");
    
    static final java.util.function.Supplier<Result> CURR_PERSON_IS_MISSING = () -> failure("Current person is missing.");
    static final String ERR_THERE_IS_NO_PERSON_ASSOCIATED_WITH_USER = "There is no " + Person.ENTITY_TITLE + " associated with User [%s].";
    
    /** Should provide a person with default user name and password. */
    Person makeUser(final Person person);

    /** Retrieves current person using the default fetch model. */
    Optional<Person> currentPerson();

    /** Retrieves current person using the fetch model provided. */
    Optional<Person> currentPerson(final fetch<Person> fetchPerson);

    /** Retrieves Person using its associated user. */
    default Person findPersonByUser(final User user) {
        final EntityResultQueryModel<Person> query = select(Person.class).where().prop("user").eq().val(user).model();
        return getEntityOptional(from(query).model()).orElseThrow(() -> Result.failuref(ERR_THERE_IS_NO_PERSON_ASSOCIATED_WITH_USER, user));
    }

    /**
     * Retrieve either current person's email, or Result if it is missing. 
     * Throws an error if current person is missing.
     * suffix - used to customise Result's message.
     */
    default Either<Result, String> getCurrentPersonsEmail(final String suffix) {
        final Person currentPerson = currentPerson().orElseThrow(CURR_PERSON_IS_MISSING);
        if (currentPerson.getEmail() == null) {
            return left(failure(format("Your personnel record is missing email address. %s", suffix)));
        } else {
            return right(currentPerson.getEmail());
        }
    }

}