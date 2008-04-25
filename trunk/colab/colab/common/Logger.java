package colab.common;

/**
 * Logger is used instead of System.out.println so that printing
 * must be specifically enabled before anything will print.
 *
 * This allows unit tests to run without dumping a bunch of
 * garbage onto their output.
 */
public final class Logger {

    /** Hidden default constructor. */
    private Logger() {
    }

    private static boolean enable = false;

    /**
     * Sets the enabling of logging statements.
     *
     * @param e to to turn logging on, false for off
     */
    public static void enable(final boolean e) {
        enable = e;
    }

    /**
     * Prints a string to System.out if logging is enabled.
     *
     * @param str the string to print
     */
    public static void log(final String str) {
        if (enable) {
            System.out.println(str);
        }
    }

}
