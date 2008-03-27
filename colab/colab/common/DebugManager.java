package colab.common;

public class DebugManager {

    /**
     * Flag for printing any exception stack trace.
     */
    private static final boolean EXCEPTIONS = true;

    /**
     * Flag for printing exceptions produced when a window
     * is closing, which can probably be disregarded.
     */
    private static final boolean WINDOW_CLOSE = true;

    /**
     * Flag for printing remote exceptions.
     */
    private static final boolean REMOTE = true;

    /**
     * Flag for printing network exceptions.
     */
    private static final boolean NETWORK = true;

    /**
     * Flag for printing illegal state exceptions.
     */
    private static final boolean ILLEGAL_STATE = true;

    private static final boolean PRINT_DEBUG_MESSAGES = true;

    public static void exception(final Throwable t) {
        if (EXCEPTIONS) {
            t.printStackTrace();
        }
    }

    public static void windowClose(final Throwable t) {
        if (WINDOW_CLOSE && EXCEPTIONS) {
            t.printStackTrace();
        }
    }

    public static void remote(final Throwable t) {
        if (REMOTE && EXCEPTIONS) {
            t.printStackTrace();
        }
    }

    public static void network(final Throwable t) {
        if (NETWORK && EXCEPTIONS) {
            t.printStackTrace();
        }
    }

    public static void illegalState(final Throwable t) {
        if (ILLEGAL_STATE && EXCEPTIONS) {
            t.printStackTrace();
        }
    }

    public static void debug(final String message) {
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("*** DEBUG *** " + message);
        }
    }

}
