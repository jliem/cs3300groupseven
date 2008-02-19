package colab.util;

public final class StringUtils {

	/** Hidden default constructor. */
	private StringUtils() {
		;
	}
	
	public static String emptyIfNull(final String str) {
		return (str != null) ? str : "";
	}
	
	public static boolean isEmptyOrNull(final String str) {
		return (str == null) || (str.equals(""));
	}
	
}
