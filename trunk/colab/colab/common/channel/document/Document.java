package colab.common.channel.document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import colab.common.channel.document.diff.DocumentParagraphDiff;
import colab.common.event.document.DocumentListener;
import colab.common.exception.NotApplicableException;
import colab.common.identity.ParagraphIdentifier;

public final class Document {

    /**
     * This document's internal paragraph-ordering.
     */
    private List<DocumentParagraph> paragraphs;

    /**
     * A list of listeners to be called whenever a paragraph is inserted
     * into or deleted from the document.
     */
    private List<DocumentListener> documentListeners;

    /**
     * Constructs an empty Document.
     */
    public Document() {

        this.paragraphs = Collections
                .synchronizedList(new ArrayList<DocumentParagraph>());
        this.documentListeners = new ArrayList<DocumentListener>();

    }

    /**
     * Inserts a {@link DocumentParagraph} into this document.
     *
     * @param paragraph
     *            a paragraph to be inserted into the document
     */
    public void insert(final ParagraphIdentifier previous,
            final DocumentParagraph paragraph) throws NotApplicableException {

        int offset = 0;
        if (previous != null) {
            boolean offsetFound = false;
            for (DocumentParagraph p : paragraphs) {
                offset++;
                if (p.getId().equals(previous)) {
                    offsetFound = true;
                    break;
                }
            }
            if (!offsetFound) {
                throw new NotApplicableException();
            }
        }
        insert(offset, paragraph);

    }

    public void insert(final int offset, final DocumentParagraph paragraph) {

        if (offset <= paragraphs.size() && offset >= 0) {
            paragraphs.add(offset, paragraph);
            fireOnInsert(offset, paragraph);
        }

    }

    /**
     * Deletes the first {@link DocumentParagraph} with a matching
     * {@link ParagraphIdentifier}, where the order of assessment matches the
     * order of logical paragraphs in the document.
     *
     * @param id
     *            the paragraph id used to determine which paragraph should be
     *            deleted. IDs should be unique per channel.
     */
    public void delete(final ParagraphIdentifier id) {
        DocumentParagraph rem = null;

        for (DocumentParagraph par : paragraphs) {
            if (par.getId().equals(id)) {
                rem = par;
                break;
            }
        }

        if (rem != null) {
            paragraphs.remove(rem);
            fireOnDelete(id);
        }
    }

    /**
     * Returns a deep copy of this Document object. Guarantees that this !=
     * this.copy(), and this.get(i) != this.copy().get(i), while retaining
     * functional equality.
     *
     * @return a new Document object
     */
    public Document copy() {
        Document cop = new Document();
        for (DocumentParagraph p : paragraphs) {
            cop.insert(cop.getNumberParagraphs(), p.copy());
        }

        return cop;
    }

    /**
     * Applies a {@link DocumentParagraphDiff} to a paragraph matching
     * <code>id</code>.
     *
     * @param id
     *            the paragraph id of a paragraph in this document
     * @param diff
     *            a change intended for the paragraph matching <code>id</code>
     * @throws NotApplicableException
     */
    public void applyEdit(final ParagraphIdentifier id,
            final DocumentParagraphDiff diff) throws NotApplicableException {

        for (DocumentParagraph par : paragraphs) {
            if (id.equals(par.getId())) {
                diff.apply(par);
                break;
            }
        }
    }

    /**
     * Retrieves the <code>(index+1)</code>th paragaph of this document.
     *
     * @param index
     *            the position of the paragraph
     * @return the <code>(index+1)</code>th paragraph
     */
    public DocumentParagraph get(final int index) {

        return paragraphs.get(index);

    }

    /**
     * Returns the size of the internal collection of paragraphs.
     *
     * @return an int representing the number of paragraphs in this document
     */
    public int getNumberParagraphs() {

        return paragraphs.size();

    }

    /**
     * Returns an iterator over the internal collection of DocumentParagraphs.
     *
     * @return a paragraph iterator
     */
    public Iterator<DocumentParagraph> paragraphIterator() {
        return paragraphs.iterator();
    }

    /**
     * Retrieves the DocumentParagraph of this document with getId().equals(id),
     * or null on no match.
     * 
     * @param id a ParagraphIdentifier signifying the paragraph to retrieve.
     * @return a DocumentParagraph or null
     */
    public DocumentParagraph get(final ParagraphIdentifier id) {
        DocumentParagraph para = null;

        Iterator<DocumentParagraph> iter = paragraphs.iterator();
        while(iter.hasNext()) {
            DocumentParagraph next = iter.next();
            if(next.getId().equals(id)) {
                para = next;
                break;
            }
        }

        return para;
    }

    public void addDocumentListener(final DocumentListener listener) {

        documentListeners.add(listener);

    }

    public void removeDocumentListener(final DocumentListener listener) {
        documentListeners.remove(listener);
    }

    protected void fireOnInsert(final int offset,
            final DocumentParagraph paragraph) {

        for (final DocumentListener listener : documentListeners) {
            listener.onInsert(offset, paragraph);
        }

    }

    protected void fireOnDelete(final ParagraphIdentifier id) {

        for (final DocumentListener listener : documentListeners) {
            listener.onDelete(id);
        }

    }

}
