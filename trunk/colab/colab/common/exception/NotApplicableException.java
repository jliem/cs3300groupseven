package colab.common.exception;

public class NotApplicableException extends Exception {
    public final static long serialVersionUID = 1;
    
    public NotApplicableException() {
        super();
    }

    public NotApplicableException(Throwable cause) {
        super(cause);
    }
    
    public NotApplicableException(final String message) {
        super(message);
    }
}
