package helsinki.personnel;

import static java.lang.String.format;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetch;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchAll;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.from;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.select;

import org.apache.log4j.Logger;
import java.util.Optional;
import com.google.inject.Inject;

import helsinki.security.tokens.personnel.MakePersonAUserToken;

import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.dao.annotations.SessionRequired;
import ua.com.fielden.platform.entity.annotation.EntityType;
import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.entity.query.fluent.fetch;
import ua.com.fielden.platform.entity.query.model.EntityResultQueryModel;
import ua.com.fielden.platform.error.Result;
import ua.com.fielden.platform.security.Authorise;
import ua.com.fielden.platform.security.user.IUser;
import ua.com.fielden.platform.security.user.User;

/**
 * DAO implementation for companion object {@link IPerson}.
 *
 * @author Generated
 *
 */
@EntityType(Person.class)
public class PersonDao extends CommonEntityDao<Person> implements IPerson {

    private static final Logger LOGGER = Logger.getLogger(PersonDao.class);

    @Inject
    protected PersonDao(final IFilter filter) {
        super(filter);
    }

    /**
     * Generates a person code that is required to be unique in the system and, possibly, short (4 letters preferred).
     *
     * @param givenName
     * @param surName
     * @param userName
     * @return
     */
    private String generateUniquePersonCode(final String givenName, final String surName, final String userName) {
        final String gn = letters(givenName, 1);
        final String four = gn + letters(surName, 3), three = gn + letters(surName, 2), two = gn + letters(surName, 1), usernameFour = letters(userName, 4);

        if (!entityWithKeyExists(four)) {
            return four;
            // error("A person code [" + code + "] is not unique. Currently just fail.");
        } else if (!entityWithKeyExists(three)) {
            return three;
        } else if (!entityWithKeyExists(two)) {
            return two;
        } else if (!entityWithKeyExists(usernameFour)) {
            return usernameFour;
        } else {
            for (char c = 'A'; c <= 'Z'; c++) {
                final String code = three + c;
                if (!entityWithKeyExists(code)) {
                    return code;
                }
            }
            for (char c = '0'; c <= '9'; c++) {
                final String code = three + c;
                if (!entityWithKeyExists(code)) {
                    return code;
                }
            }
            final String ex = "A new user/person creation failed for [" + givenName + " " + surName + "] with automatically generated username [" + userName
                    + "]. All the following usernames are regretably already assigned to other persons: [" + four + "," + three + "," + two + "," + usernameFour
                    + "] and all codes [" + three + " + letter/digit]. Is it possible? Please contact your administrator.";
            LOGGER.error(ex);
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Returns first n letters (or digits) in upper case form.
     *
     * @param i
     * @param givenName
     * @return
     */
    private static String letters(final String str, final int n) {
        final String cleanUpperCase = str.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        return cleanUpperCase.substring(0, n);
    }

    /**
     * Generates person description.
     *
     * @param givenName
     * @param surName
     * @param fullName
     * @return
     */
    public static String generatePersonDesc(final String givenName, final String surName, final String fullName) {
        // please note that a fullName, as was specified in LDAP server, could be used as description too, but for now (as in T32) : surName + " " + givenName :
        return surName + " " + givenName;
    }

    @Override
    public Optional<Person> currentPerson(final fetch<Person> fetchPerson) {
        final EntityResultQueryModel<Person> qExecModel = select(Person.class).where().prop("user").eq().val(getUser()).model();
        return getEntityOptional(from(qExecModel).with(fetchPerson).model());
    }

    @Override
    public Optional<Person> currentPerson() {
        return currentPerson(fetch(Person.class));
    }

    @Override
    protected IFetchProvider<Person> createFetchProvider() {
        return IPerson.FETCH_PROVIDER;
    }

    @Override
    @Authorise(MakePersonAUserToken.class)
    @SessionRequired
    public Person makeUser(final Person person) {
        if (person.isUser()) {
            throw Result.failure(format("Person [%s] is already an application user.", person.getKey()));
        }

        final String email = person.getEmail();
        final boolean active = email != null; // i.e. only make a user active if the email is not null
        final User user = co(User.class).new_().setKey(person.getKey()).setEmail(email).setActive(active);
        user.setDesc(format("User for person [%s].", person.getDesc()));
        final IUser coUser = co(User.class);
        final User su = coUser.findByKeyAndFetch(fetchAll(User.class), User.system_users.SU.name());
        user.setBasedOnUser(su);
        final User savedUser = coUser.resetPasswd(user, user.getKey()).getKey();

        final Person latestPerson = findById(person.getId(), fetchAll(Person.class));
        latestPerson.setUser(savedUser);
        return save(latestPerson);
    }
}
