package colab.common;

public class DebugManager {

    /**
     * Flag for printing any exception stack trace.
     */
    public static final boolean EXCEPTIONS = true;

    /**
     * Flag for printing exceptions produced when a window is closing, which
     * can probably be disregarded.
     */
    public static final boolean EXIT = EXCEPTIONS && true;

    /**
     * Flag for printing remote exceptions.
     */
    public static final boolean REMOTE = EXCEPTIONS && true;

    /**
     * Flag for printing network exceptions.
     */
    public static final boolean NETWORK = EXCEPTIONS && true;

    /**
     * Flag for printing illegal state exceptions.
     */
    public static final boolean ILLEGAL_STATE = EXCEPTIONS && true;

    public static final boolean PRINT_DEBUG_MESSAGES = true;

    public static void debug(String message) {
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("*** DEBUG *** " + message);
        }
    }
}
