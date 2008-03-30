package colab.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import colab.common.identity.Identifiable;
import colab.common.identity.Identifier;
import colab.common.identity.ParagraphIdentifier;
import colab.common.naming.UserName;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlSerializable;

public final class DocumentParagraph
        implements Serializable, Identifiable, XmlSerializable {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private List<ParagraphListener> listeners;

    private int headerLevel;

    private StringBuffer contents;

    private UserName lockHolder;

    private DocumentParagraphDiff differences;

    private ParagraphIdentifier id;

    public DocumentParagraph() {
        this("", 0, null, new Date());
    }

    public DocumentParagraph(final String cont, final int head,
            final UserName creator, final Date date) {
        headerLevel = head;
        contents = new StringBuffer(cont);
        lockHolder = creator;
        id = new ParagraphIdentifier(creator, date);
        differences = new DocumentParagraphDiff();
        listeners = new ArrayList<ParagraphListener>();
    }

    protected DocumentParagraph(final String cont, final int head,
            final UserName creator, final DocumentParagraphDiff diff) {
        this(cont, head, creator, new Date());
        differences = diff;
    }

    public void insert(final int offset, final String hunk) {
        contents.insert(offset, hunk);
    }

    public void delete(final int offset, final int length) {
        contents.delete(offset, length);
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
        return new DocumentParagraph(
                this.contents.toString(),
                this.headerLevel,
                this.lockHolder,
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

    protected void fireOnLock(final UserName newOwner) {
        for (ParagraphListener listener : listeners) {
            listener.onLock(newOwner);
        }
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

    /** {@inheritDoc} */
    public XmlNode toXml() {
        XmlNode node = new XmlNode("Paragraph");
        node.setAttribute("id", id.toString());
        node.setAttribute("headerLevel", Integer.toString(headerLevel));
        node.setContent(contents.toString());
        return node;
    }

    public DocumentParagraph(final XmlNode node) {
        this.id = new ParagraphIdentifier(
                Integer.parseInt(node.getAttribute("id")));
    }

}

interface ParagraphListener {

    public void onLock(UserName newOwner);

    public void onUnlock();

    public void onHeaderChange(int headerLevel);

    public void onInsert(int offset, String hunk);

    public void onDelete(int offset, int length);

}
