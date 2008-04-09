package colab.common.exception;

public class NotApplicableException extends Exception {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    public NotApplicableException() {
        super();
    }

    public NotApplicableException(final Throwable cause) {
        super(cause);
    }

    public NotApplicableException(final String message) {
        super(message);
    }
}
