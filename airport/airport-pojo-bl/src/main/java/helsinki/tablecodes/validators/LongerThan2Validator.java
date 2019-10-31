package helsinki.tablecodes.validators;

import static ua.com.fielden.platform.error.Result.failure;

import java.lang.annotation.Annotation;
import java.util.Set;

import ua.com.fielden.platform.entity.meta.MetaProperty;
import ua.com.fielden.platform.entity.validation.IBeforeChangeEventHandler;
import ua.com.fielden.platform.error.Result;

public class LongerThan2Validator implements IBeforeChangeEventHandler<String> {

    public static final String ERR_SHOULD_BE_LONGER_THAN = "Value should be longer than 2.";
    
    @Override
    public Result handle(final MetaProperty<String> property, final String newValue, final Set<Annotation> mutatorAnnotations) {
        if (newValue.length() < 3) {
            return failure(ERR_SHOULD_BE_LONGER_THAN);
        }

        return Result.successful("All is good");
    }

}
