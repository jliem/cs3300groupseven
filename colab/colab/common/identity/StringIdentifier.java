package colab.common.identity;

/**
 * An Identifier that uses a String as its data field.
 */
public abstract class StringIdentifier extends Identifier<String> {

    /**
     * Constructs an empty string identifier.
     */
    public StringIdentifier() {
        super();
    }

    /**
     * Constructs a string identifier with a given value.
     *
     * @param str the value of the identifying string
     */
    public StringIdentifier(final String str) {
        super(str);
    }

    /**
     * @return the identifying string.
     */
    @Override
    public final String toString() {
        return getValue();
    }

    /**
     * Compares this object with the specified object for order.
     *
     * @param stringIdentifier the object to compare to
     * @return a negative integer, zero, or a positive integer
     * as this object is less than, equal to, or greater than
     * the specified object.
     */
    public final int compareTo(final StringIdentifier stringIdentifier) {
        return this.toString().compareTo(stringIdentifier.toString());
    }
}
