package colab.common;

import java.util.ArrayList;
import java.util.List;

import colab.common.identity.ParagraphIdentifier;

public final class Document {

    private List<DocumentParagraph> paragraphs;

    private List<InsertParagraphListener> insertListeners;

    private List<DeleteParagraphListener> deleteListeners;

    /**
     * Constructs an empty Document.
     */
    public Document() {

        this.paragraphs = new ArrayList<DocumentParagraph>();
        this.insertListeners = new ArrayList<InsertParagraphListener>();
        this.deleteListeners = new ArrayList<DeleteParagraphListener>();

    }

    public void insert(final int offset, final DocumentParagraph paragraph) {

        if (offset <= paragraphs.size() && offset >= 0) {
            paragraphs.add(offset, paragraph);
            fireOnInsert(offset, paragraph);
        }

    }

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

    public void applyEdit(final ParagraphIdentifier id,
            final DocumentParagraphDiff diff) {

        for (DocumentParagraph par : paragraphs) {
            if (id.equals(par.getId())) {
                diff.apply(par);
                break;
            }
        }

    }

    public DocumentParagraph get(final int index) {

        return paragraphs.get(index);

    }

    public int getNumberParagraphs() {

        return paragraphs.size();

    }

    public void addInsertParagraphListener(
            final InsertParagraphListener listener) {

        insertListeners.add(listener);

    }

    public void addDeleteParagraphListener(
            final DeleteParagraphListener listener) {

        deleteListeners.add(listener);

    }

    protected void fireOnInsert(final int offset,
            final DocumentParagraph paragraph) {

        for (final InsertParagraphListener listener : insertListeners) {
            listener.onInsert(offset, paragraph);
        }

    }

    protected void fireOnDelete(final ParagraphIdentifier id) {

        for (final DeleteParagraphListener listener : deleteListeners) {
            listener.onDelete(id);
        }

    }

}

interface InsertParagraphListener {

    void onInsert(int offset, DocumentParagraph paragraph);

}

interface DeleteParagraphListener {

    void onDelete(ParagraphIdentifier id);

}
