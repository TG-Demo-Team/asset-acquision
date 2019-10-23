package helsinki.common.exceptions;

/**
 * An exception type of generic nature.
 * It should be used in the application code where no other application-specific exception type is suitable.
 * 
 * @author Generated
 *
 */
public class GenericApplicationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public GenericApplicationException(final String msg) {
        super(msg);
    }

    public GenericApplicationException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}