package colab.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import colab.common.identity.Identifiable;
import colab.common.identity.Identifier;
import colab.common.identity.ParagraphIdentifier;
import colab.common.naming.UserName;

public final class DocumentParagraph implements Serializable, Identifiable {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private List<ParagraphListener> listeners;

    private int headerLevel;

    private StringBuffer contents;

    private UserName lockHolder;

    private DocumentParagraphDiff differences;

    private ParagraphIdentifier id;

    public DocumentParagraph() {
        this("", 0, null, null, new Date());
    }

    public DocumentParagraph(final String cont, final int head,
            final UserName creator, final ParagraphIdentifier id,
            final Date date) {

        this.headerLevel = head;
        this.contents = new StringBuffer(cont);
        this.lockHolder = creator;
        this.id = id;

        this.differences = new DocumentParagraphDiff();
        this.listeners = new ArrayList<ParagraphListener>();

    }

    protected DocumentParagraph(final String cont, final int head,
            final UserName creator, final ParagraphIdentifier id,
            final DocumentParagraphDiff diff) {

        this(cont, head, creator, id, new Date());
        differences = diff;

    }

    public void setId(final ParagraphIdentifier id) {
        this.id = id;
    }

    public void insert(final int offset, final String hunk) {
        contents.insert(offset, hunk);
        fireOnInsert(offset, hunk);
    }

    public void delete(final int offset, final int length) {
        contents.delete(offset, length);
        fireOnDelete(offset, length);
    }

    public int getLength() {
        return contents.length();
    }

    public int getHeaderLevel() {
        return headerLevel;
    }

    public void setHeaderLevel(final int headerLevel) {
        if (headerLevel > 0) {
            this.headerLevel = headerLevel;
        } else {
            this.headerLevel = 0;
        }
        fireHeaderChange(headerLevel);
    }

    public void unlock() {
        lockHolder = null;
        fireOnUnlock();
    }

    public boolean isLocked() {
        return (lockHolder != null);
    }

    /**
     * @param holder -
     *            the UserName that will hold the lock of this paragraph
     * @return a boolean for success- returns false if lock already held
     */
    public boolean lock(final UserName holder) {
        boolean ret = false;

        if (holder == null) {
            unlock();
            return true;
        }

        if (lockHolder == null) {
            lockHolder = holder;
            ret = true;
            fireOnLock(holder);
        }

        return ret;
    }

    public UserName getLockHolder() {
        return lockHolder;
    }

    public String getContents() {
        return contents.toString();
    }

    public DocumentParagraph copy() {

        /* TODO: username copy method, otherwise copy could
         * change lock in this instantiation */

        return new DocumentParagraph(this.contents.toString(),
                this.headerLevel, this.lockHolder,
                new ParagraphIdentifier(this.id),
                this.differences.copy());

    }

    public DocumentParagraphDiff getDifferences() {

        DocumentParagraphDiff ret = differences.copy();

        resetDifferences();

        return ret;

    }

    public DocumentParagraphDiff peekDifferences() {
        return differences.copy();
    }

    public void resetDifferences() {
        differences.reset();
    }

    public boolean hasChanged() {
        return differences.hasChanges();
    }

    public Identifier getId() {
        return id;
    }

    public void addParagraphListener(final ParagraphListener listener) {
        listeners.add(listener);
    }

    public void removeParagraphListener(final ParagraphListener listener) {
        listeners.remove(listener);
    }

    protected void fireOnLock(final UserName newOwner) {
        for (ParagraphListener listener : listeners) {
            listener.onLock(newOwner);
        }

        System.err.println("Lock fired from paragraph.");
    }

    protected void fireOnUnlock() {
        for (ParagraphListener listener : listeners) {
            listener.onUnlock();
        }
    }

    protected void fireHeaderChange(final int headerLevel) {
        for (ParagraphListener listener : listeners) {
            listener.onHeaderChange(headerLevel);
        }
    }

    protected void fireOnInsert(final int offset, final String hunk) {
        for (ParagraphListener listener : listeners) {
            listener.onInsert(offset, hunk);
        }
    }

    protected void fireOnDelete(final int offset, final int length) {
        for (ParagraphListener listener : listeners) {
            listener.onDelete(offset, length);
        }
    }

}
