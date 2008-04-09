package colab.common.identity;

import colab.common.channel.ChannelDataIdentifier;


/**
 * An Identifier used to identify paragraphs.
 *
 * Within a document, a paragraph's identifier must be unique for all time;
 * that is, a paragraph editor cannot be re-used after a paragraph is deleted.
 */
public final class ParagraphIdentifier extends ChannelDataIdentifier {

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
     * value of a ChannelDataIdentifier.
     *
     * @param otherId the identifier to copy
     */
    public ParagraphIdentifier(final ChannelDataIdentifier otherId) {
        super(otherId.getValue());
    }

}
