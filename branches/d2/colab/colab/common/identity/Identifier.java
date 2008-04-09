package colab.common.identity;

import java.io.Serializable;

/**
 * An Identifier is an object that serves as the id attribute
 * of an {@link Identifiable} object.
 *
 * @param <T> the underlying type that this identifier represents
 *            (such  as a string for a name, or an int for an ID number)
 */
public abstract class Identifier<T extends Comparable<T>>
        implements Serializable, Comparable<Identifier<T>> {

    /**
     * The identifying data.
     */
    private T value;

    /**
     * Constructs an identifier with a null value.
     */
    public Identifier() {
        this.value = null;
    }

    /**
     * Constructs an identifier with a given value.
     *
     * @param value the value of the identifier
     */
    public Identifier(final T value) {
        this.value = value;
    }

    /**
     * Constructs an identifier with the value of another identifier.
     *
     * @param otherId the identifier to copy
     */
    public Identifier(final Identifier<T> otherId) {
        this.value = otherId.value;
    }

    /**
     * Returns the value of this identifier.
     *
     * @return the data represented by this object
     */
    public final T getValue() {
        return value;
    }

    public final void setValue(final T value) {
        this.value = value;
    }

    /** {@inheritDoc} */
    @Override
    public final int hashCode() {
        if (value == null) {
            return 0;
        } else {
            return value.hashCode();
        }
    }

    /** {@inheritDoc} */
    @Override
    public final boolean equals(final Object obj) {

        if (!(obj instanceof Identifier)) {
            return false;
        }

        Identifier other = (Identifier) obj;
        return other.value.equals(this.value);

    }

    /** {@inheritDoc} */
    public final int compareTo(final Identifier<T> otherId) {
        return value.compareTo(otherId.value);
    }

}
