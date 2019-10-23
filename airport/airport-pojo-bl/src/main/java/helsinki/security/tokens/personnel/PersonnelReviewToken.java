package helsinki.security.tokens.personnel;

import ua.com.fielden.platform.entity.annotation.KeyTitle;
import ua.com.fielden.platform.security.ISecurityToken;

/**
 * Personnel review security token.
 *
 * @author TG Team
 *
 */
@KeyTitle(value = "Personnel review", desc = "Controls permission to select and review personnel data.")
public class PersonnelReviewToken implements ISecurityToken {
}
