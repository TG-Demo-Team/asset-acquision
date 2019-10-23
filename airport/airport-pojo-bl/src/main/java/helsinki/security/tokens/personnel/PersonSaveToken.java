package helsinki.security.tokens.personnel;

import ua.com.fielden.platform.entity.annotation.KeyTitle;

/**
 * Save person security token.
 *
 * @author TG Team
 *
 */
@KeyTitle(value = "Person save", desc = "Controls permission to save personnel changes.")
public class PersonSaveToken extends PersonnelReviewToken {
}
