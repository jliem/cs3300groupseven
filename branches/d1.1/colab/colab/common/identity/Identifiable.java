package colab.common.identity;

/**
 * An Identifiable class has a unique identifying property,
 * such as a name or ID number.
 *
 * @param <T> the type of identifier used by this class
 */
public interface Identifiable<T extends Identifier> {

    /**
     * Gets the value of this object's id.
     *
     * @return the object that identifies this object.
     */
    T getId();

}
