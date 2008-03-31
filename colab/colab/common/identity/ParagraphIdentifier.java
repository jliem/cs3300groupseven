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

    public ParagraphIdentifier(final int id) {
        super(id);
    }

}
