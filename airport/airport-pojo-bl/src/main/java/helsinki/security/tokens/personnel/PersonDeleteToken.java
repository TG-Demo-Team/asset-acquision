package helsinki.security.tokens.personnel;

import ua.com.fielden.platform.entity.annotation.KeyTitle;

@KeyTitle(value = "Person delete", desc = "Controls permission to delete personnel data.")
public class PersonDeleteToken extends PersonnelReviewToken {

}
