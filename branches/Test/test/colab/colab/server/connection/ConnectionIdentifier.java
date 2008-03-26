package colab.server.connection;

import colab.common.identity.IntegerIdentifier;

/**
 * An integer identifier for a {@link Connection}.
 */
public class ConnectionIdentifier extends IntegerIdentifier {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * Constructs an connection identifier with a given value.
     *
     * @param integer the value of the identifying integer
     */
    public ConnectionIdentifier(final Integer integer) {
        super(integer);
    }

    /**
     * Constructs an connection identifier with a given value.
     *
     * @param integer the value of the identifying integer
     */
    public ConnectionIdentifier(final int integer) {
        this(new Integer(integer));
    }

}
