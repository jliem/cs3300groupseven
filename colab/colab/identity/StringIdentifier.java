package colab.identity;

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

}
