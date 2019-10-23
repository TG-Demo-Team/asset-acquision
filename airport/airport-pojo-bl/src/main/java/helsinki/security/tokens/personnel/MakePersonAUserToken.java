package helsinki.security.tokens.personnel;

import ua.com.fielden.platform.entity.annotation.KeyTitle;

/**
 * Make person a user security token.
 *
 * @author TG Team
 *
 */
@KeyTitle(value = "Make/unmake person a user", desc = "Controls permission to make/unmake any person a valid application user.")
public class MakePersonAUserToken extends PersonnelReviewToken {
}
