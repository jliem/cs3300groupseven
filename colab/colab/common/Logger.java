package colab.common;

public final class Logger {

    /** Hidden default constructor. */
    private Logger() {
    }

    private static boolean enable = false;

    public static void enable(final boolean e) {
        enable = e;
    }

    public static void log(final String str) {
        if (enable) {
            System.out.println(str);
        }
    }

}
