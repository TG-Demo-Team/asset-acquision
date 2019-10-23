package helsinki.security.tokens.personnel;

import ua.com.fielden.platform.entity.annotation.KeyTitle;

/**
 * Reset user password security token.
 *
 * @author TG Team
 *
 */
@KeyTitle(value = "Reset user password", desc = "Controls permission to reset user password.")
public class ResetUserPasswordToken extends PersonnelReviewToken {
}
