package colab.common.xml;

/**
 * Indicates that an improperly-formatted xml document could not be parsed.
 *
 * Can also indicate that data found within an xml document cannot be parsed
 * (such as an improperly formatted date).
 */
public class XmlParseException extends Exception {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * Constructs an empty XmlParseException.
     */
    public XmlParseException() {
    }

    /**
     * Constructs an XmlParseException with the specified cause.
     *
     * @param cause the exception that caused this one to be thrown
     */
    public XmlParseException(final Throwable cause) {
        super(cause);
    }

}
