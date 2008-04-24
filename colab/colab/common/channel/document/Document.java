package colab.common.channel.document;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import colab.common.channel.document.diff.DocumentParagraphDiff;
import colab.common.event.document.DocumentListener;
import colab.common.exception.NotApplicableException;
import colab.common.identity.ParagraphIdentifier;

/**
 * A document is a logical ordered collection of paragraphs.
 */
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
     * @param previous the id of the paragraph after which to insert
     * @param paragraph a paragraph to be inserted into the document
     * @throws NotApplicableException if the insert fails
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

    /**
     * Inserts a paragraph into the document at a given offset.
     *
     * @param offset the offset at which to insert
     * @param paragraph the paragraph to insert
     */
    public void insert(final int offset, final DocumentParagraph paragraph) {

        if (paragraph == null) {
            throw new IllegalArgumentException("Can't insert a null paragraph");
        }

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
            if (p != null) {
                cop.insert(cop.getNumberParagraphs(), p.copy());
            }
        }

        return cop;
    }

    /**
     * Applies a {@link DocumentParagraphDiff} to a paragraph matching id.
     *
     * @param id the paragraph id of a paragraph in this document
     * @param diff a change intended for the paragraph matching id
     * @throws NotApplicableException if the insert fails
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
     * Retrieves the (index+1)th paragaph of this document.
     *
     * @param index the position of the paragraph
     * @return the (index+1)th paragraph
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

    /**
     * @param listener a listener to add
     */
    public void addDocumentListener(final DocumentListener listener) {

        documentListeners.add(listener);

    }

    /**
     * @param listener a listener to remove
     */
    public void removeDocumentListener(final DocumentListener listener) {
        documentListeners.remove(listener);
    }

    private void fireOnInsert(final int offset,
            final DocumentParagraph paragraph) {

        for (final DocumentListener listener : documentListeners) {
            listener.onInsert(offset, paragraph);
        }

    }

    private void fireOnDelete(final ParagraphIdentifier id) {

        for (final DocumentListener listener : documentListeners) {
            listener.onDelete(id);
        }

    }

    /**
     * Converts the document contents to html form, and writes it
     * onto the given writer.
     *
     * @param writer the writer onto which to write html
     * @throws IOException if an I/O error occurs in writing the html
     */
    public void export(final PrintWriter writer) throws IOException {

        for (final DocumentParagraph paragraph: this.paragraphs) {
            writer.println(paragraph.toHtml());
        }

    }

}
