package colab.common.identity;

/**
 * An Identifier that uses a String as its data field.
 */
public abstract class IntegerIdentifier extends Identifier<Integer> {

    /**
     * Constructs an empty integer identifier.
     */
    public IntegerIdentifier() {
        super();
    }

    /**
     * Constructs an integer identifier with a given value.
     *
     * @param integer the value of the identifying integer
     */
    public IntegerIdentifier(final Integer integer) {
        super(integer);
    }

    /**
     * Constructs an integer identifier with a given value.
     *
     * @param integer the value of the identifying integer
     */
    public IntegerIdentifier(final int integer) {
        this(new Integer(integer));
    }

    /**
     * @return the identifying integer as a string.
     */
    @Override
    public final String toString() {
        return getValue().toString();
    }

}
