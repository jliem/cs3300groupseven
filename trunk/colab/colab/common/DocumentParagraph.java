package colab.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import colab.common.identity.Identifiable;
import colab.common.identity.Identifier;
import colab.common.identity.ParagraphIdentifier;
import colab.common.naming.UserName;

public class DocumentParagraph implements Serializable, Identifiable {
    public static final long serialVersionUID = 1;

    private ArrayList<ParagraphListener> listeners;

    private int headerLevel;

    private StringBuffer contents;

    private UserName lockHolder;

    private DocumentParagraphDiff differences;

    private ParagraphIdentifier id;

    public DocumentParagraph() {
        this("", 0, null, new Date());
    }

    public DocumentParagraph(final String cont, int head, UserName creator, Date date) {
        headerLevel = head;
        contents = new StringBuffer(cont);
        lockHolder = creator;
        id = new ParagraphIdentifier(creator, date);
        differences = new DocumentParagraphDiff();
        listeners = new ArrayList<ParagraphListener>();
    }

    protected DocumentParagraph(String cont, int head, UserName creator, DocumentParagraphDiff diff) {
        this(cont, head, creator, new Date());
        differences = diff;
    }

    public void insert(int offset, String hunk) {
        contents.insert(offset, hunk);
    }

    public void delete(int offset, int length) {
        contents.delete(offset, length);
    }

    public int getLength() {
        return contents.length();
    }

    public int getHeaderLevel() {
        return headerLevel;
    }

    public void setHeaderLevel(int headerLevel) {
        if (headerLevel > 0) {
            this.headerLevel = headerLevel;
        } else {
            this.headerLevel = 0;
        }
    }

    public void unlock() {
        lockHolder = null;
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

        if (lockHolder == null) {
            lockHolder = holder;
            ret = true;
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
        // TODO: username copy method, otherwise copy could change lock in this
        // instantiation
        return new DocumentParagraph(this.contents.toString(), this.headerLevel, this.lockHolder,
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

    public void addParagraphListener(ParagraphListener listener) {
        listeners.add(listener);
    }
    
    protected void fireOnLock(UserName newOwner) {
        for(ParagraphListener listener : listeners) {
            listener.onLock(newOwner);
        }
    }
    
    protected void fireOnUnlock() {
        for(ParagraphListener listener : listeners) {
            listener.onUnlock();
        }
    }
    
    protected void fireHeaderChange(int headerLevel) {
        for(ParagraphListener listener : listeners) {
            listener.onHeaderChange(headerLevel);
        }
    }
    
    protected void fireOnInsert(int offset, String hunk) {
        for(ParagraphListener listener : listeners) {
            listener.onInsert(offset, hunk);
        }
    }
    
    protected void fireOnDelete(int offset, int length) {
        for(ParagraphListener listener : listeners) {
            listener.onDelete(offset, length);
        }
    }
}

interface ParagraphListener {
    public void onLock(UserName newOwner);

    public void onUnlock();

    public void onHeaderChange(int headerLevel);

    public void onInsert(int offset, String hunk);

    public void onDelete(int offset, int length);
}