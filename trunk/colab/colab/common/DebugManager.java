package colab.common;

/**
 * DebugMananger provides static methods for printing errors.
 */
public final class DebugManager {

    /** Hidden default constructor. */
    private DebugManager() {
    }

    /**
     * Flag for printing any exception stack trace whether low,
     * normal or high priority. When possible, use the
     * priority flags instead of switching this one off.
     */
    private static boolean exceptions = false;

    /**
     * Flag for printing debug messages.
     */
    private static final boolean PRINT_DEBUG_MESSAGES = true;

    /**
     * Flag for printing any exception stack trace (it will probably
     * be ignored or the stack trace is not useful, like if the window
     * is closing).
     */
    private static final boolean LOW_PRIORITY = true;

    /**
     * Flag for printing any normal exception stack trace.
     */
    private static final boolean NORMAL_PRIORITY = true;

    /**
     * Flag for printing any high priority exception stack trace
     * (i.e. we don't mind it showing up in the demo because it might
     * not be our fault).
     */
    private static final boolean HIGH_PRIORITY = true;

    /**
     * Flag for printing exceptions that
     * we expect should never happen (and therefore
     * are extremely high priority).
     */
    private static final boolean SHOULD_NOT_HAPPEN = true;

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

    /**
     * Flag for printing connection dropped exceptions.
     */
    private static final boolean CONNECTION_DROPPED = true;

    /**
     * Flag for printing i/o exceptions.
     */
    private static final boolean IO = true;

    /**
     * Flag for printing unable to connect exceptions.
     */
    private static final boolean UNABLE_TO_CONNECT = true;

    public static void enableExceptions(final boolean enable) {
        exceptions = enable;
    }

    /**
     * Handles general exceptions.
     * @param t the Throwable
     */
    public static void exception(final Throwable t) {
        if (exceptions) {
            t.printStackTrace();
        }
    }


    /**
     * Handle exceptions thrown that we expect should never happen.
     * @param t the Throwable
     */
    public static void shouldNotHappen(final Throwable t) {
        if (SHOULD_NOT_HAPPEN && HIGH_PRIORITY && exceptions) {
            t.printStackTrace();
        }
    }

    /**
     * Handles exceptions which occur while a window is closing.
     * @param t the Throwable
     */
    public static void windowClose(final Throwable t) {
        if (WINDOW_CLOSE && exceptions && LOW_PRIORITY) {
            t.printStackTrace();
        }
    }

    /**
     * Handles remote exceptions.
     * @param t the Throwable
     */
    public static void remote(final Throwable t) {
        if (REMOTE && exceptions && NORMAL_PRIORITY) {
            t.printStackTrace();
        }
    }

    /**
     * Handles network exceptions.
     * @param t the Throwable
     */
    public static void network(final Throwable t) {
        if (NETWORK && exceptions && NORMAL_PRIORITY) {
            t.printStackTrace();
        }
    }

    /**
     * Handles illegal state exceptions.
     * @param t the Throwable
     */
    public static void illegalState(final Throwable t) {
        if (ILLEGAL_STATE && exceptions && NORMAL_PRIORITY) {
            t.printStackTrace();
        }
    }

    /**
     * Handles io exceptions.
     * @param t the Throwable
     */
    public static void ioException(final Throwable t) {
        if (IO && exceptions && NORMAL_PRIORITY) {
            t.printStackTrace();
        }
    }

    /**
     * Handle connection dropped exceptions.
     * @param t the Throwable
     */
    public static void connectionDropped(final Throwable t) {
        if (CONNECTION_DROPPED && exceptions && NORMAL_PRIORITY) {
            t.printStackTrace();
        }
    }

    /**
     * Handle exceptions thrown when a connection was not made.
     * @param t the Throwable
     */
    public static void unableToConnect(final Throwable t) {
        if (UNABLE_TO_CONNECT && exceptions && NORMAL_PRIORITY) {
            t.printStackTrace();
        }
    }

    /**
     * Prints a debugging message to the console, if enabled.
     *
     * @param message the message to print
     */
    public static void debug(final String message) {
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("*** DEBUG *** " + message);
        }
    }

}
