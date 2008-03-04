package colab.server.connection;

import colab.common.identity.IntegerIdentifier;

public class ConnectionIdentifier extends IntegerIdentifier {

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
