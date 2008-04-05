package colab.common.channel.document;

import colab.common.identity.ParagraphIdentifier;

/**
 * A listener for multiple events on a Document.
 *
 * @see Document
 */
public interface DocumentListener {

    /**
     * Called whenever a paragraph is inserted into the document.
     *
     * @param offset - the index after which the paragraph was inserted
     * @param paragraph - the paragraph that was inserted
     */
    void onInsert(int offset, DocumentParagraph paragraph);

    /**
     * Called whenever a paragraph is deleted from this document.
     *
     * @param id - the ParagraphIdentifier of the DocumentParagraph that was deleted
     */
    void onDelete(ParagraphIdentifier id);
}
