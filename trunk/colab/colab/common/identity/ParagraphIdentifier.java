package colab.common.identity;

/**
 * An Identifier used to identify paragraphs.
 *
 * Within a document, a paragraph's identifier must be unique for all time;
 * that is, a paragraph editor cannot be re-used after a paragraph is deleted.
 */
public final class ParagraphIdentifier extends IntegerIdentifier {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * Constructs a paragraph identifier with a given value.
     *
     * @param id the value of the identifying integer
     */
    public ParagraphIdentifier(final Integer id) {
        super(id);
    }

    /**
     * Constructs a paragraph identifier with the
     * value of an IntegerIdentifier.
     *
     * @param otherId the identifier to copy
     */
    public ParagraphIdentifier(final IntegerIdentifier otherId) {
        super(otherId.getValue());
    }

}
