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

    /**
     * Flag for printing connection dropped exceptions
     */
    private static final boolean CONNECTION_DROPPED = true;

    /**
     * Flag for printing i/o exceptions
     */
    private static final boolean IO = true;

    private static final boolean PRINT_DEBUG_MESSAGES = true;

    /**
     * Handles general exceptions.
     * @param t the Throwable
     */
    public static void exception(final Throwable t) {
        if (EXCEPTIONS) {
            t.printStackTrace();
        }
    }

    /**
     * Handles exceptions which occur while a window is closing.
     * @param t the Throwable
     */
    public static void windowClose(final Throwable t) {
        if (WINDOW_CLOSE && EXCEPTIONS) {
            t.printStackTrace();
        }
    }

    /**
     * Handles remote exceptions.
     * @param t the Throwable
     */
    public static void remote(final Throwable t) {
        if (REMOTE && EXCEPTIONS) {
            t.printStackTrace();
        }
    }

    /**
     * Handles network exceptions.
     * @param t the Throwable
     */
    public static void network(final Throwable t) {
        if (NETWORK && EXCEPTIONS) {
            t.printStackTrace();
        }
    }

    /**
     * Handles illegal state exceptions.
     * @param t the Throwable
     */
    public static void illegalState(final Throwable t) {
        if (ILLEGAL_STATE && EXCEPTIONS) {
            t.printStackTrace();
        }
    }

    /**
     * Handles io exceptions.
     * @param t the Throwable
     */
    public static void ioException(final Throwable t) {
        if (IO && EXCEPTIONS) {
            t.printStackTrace();
        }
    }

    /**
     * Handle connection dropped exceptions.
     * @param t the Throwable
     */
    public static void connectionDropped(final Throwable t) {
        if (CONNECTION_DROPPED && EXCEPTIONS) {
            t.printStackTrace();
        }
    }

    public static void debug(final String message) {
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("*** DEBUG *** " + message);
        }
    }

}
