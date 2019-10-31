package helsinki.tablecodes.validators;

import static ua.com.fielden.platform.error.Result.failure;

import java.lang.annotation.Annotation;
import java.util.Set;

import ua.com.fielden.platform.entity.meta.MetaProperty;
import ua.com.fielden.platform.entity.validation.IBeforeChangeEventHandler;
import ua.com.fielden.platform.error.Result;

public class LongerThanValidator implements IBeforeChangeEventHandler<String> {

    public static final String ERR_SHOULD_BE_LONGER_THAN = "Value should be at least %s characters.";
    
    private int minLength = 3;
    
    @Override
    public Result handle(final MetaProperty<String> property, final String newValue, final Set<Annotation> mutatorAnnotations) {
        if (newValue.length() < minLength) {
            return failure(String.format(ERR_SHOULD_BE_LONGER_THAN, minLength));
        }

        return Result.successful("All is good");
    }

}
