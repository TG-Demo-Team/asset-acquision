package helsinki.tablecodes.validators;

import static ua.com.fielden.platform.error.Result.failure;

import java.lang.annotation.Annotation;
import java.util.Set;

import ua.com.fielden.platform.entity.meta.MetaProperty;
import ua.com.fielden.platform.entity.validation.IBeforeChangeEventHandler;
import ua.com.fielden.platform.error.Result;

public class NoSpacesValidator implements IBeforeChangeEventHandler<String> {

    public static final String ERR_NO_SPACES_ALLOWED = "No spaces allowed.";
    
    @Override
    public Result handle(final MetaProperty<String> property, final String newValue, final Set<Annotation> mutatorAnnotations) {
        if (newValue.contains(" ")) {
            return failure(ERR_NO_SPACES_ALLOWED);
        }
        
        return Result.successful("All is good");
    }

}
