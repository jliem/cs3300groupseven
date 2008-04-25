package colab.common.channel.document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import colab.common.channel.document.diff.DocumentParagraphDiff;
import colab.common.event.document.ParagraphListener;
import colab.common.identity.Identifiable;
import colab.common.identity.ParagraphIdentifier;
import colab.common.naming.UserName;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;
import colab.common.xml.XmlSerializable;

/**
 * A logical paragraph, to be used as the basis for our documents.
 *
 * @see Document
 */
public final class DocumentParagraph implements Serializable,
        Identifiable<ParagraphIdentifier>, XmlSerializable {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private List<ParagraphListener> listeners;

    private int headerLevel;

    private StringBuffer contents;

    private UserName lockHolder;

    private DocumentParagraphDiff differences;

    private ParagraphIdentifier id;


    /**
     * Constructs an empty DocumentParagraph without a lock holder.
     */
    public DocumentParagraph() {
        this("", 0, null, null, new Date());
    }

    /**
     * Constructs a DocumentParagaph with an identifier, contents,
     * a header level, and a creation date.
     *
     * @param contents the textual contents of the paragraph
     * @param head the header level of this paragrap, starting at 0
     * @param creator the creator of the paragraph, to be the first lock holder
     * @param id an object identifying this paragraph with a document
     * @param date the creation date of the paragraph
     */
    public DocumentParagraph(final String contents, final int head,
            final UserName creator, final ParagraphIdentifier id,
            final Date date) {

        this.headerLevel = head;
        this.contents = new StringBuffer(contents);
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

    public void insert(final int offset, final String hunk) {

        contents.insert(offset, hunk);

        fireOnInsert(offset, hunk);

    }

    public void delete(final int offset, final int length) {

        contents.delete(offset, (length + offset));

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
     * @param holder - the UserName that will hold the lock of this paragraph
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

    /**
     * Fetches the textual content of this paragaph.
     *
     * @return the contents
     */
    public String getContents() {
        return contents.toString();
    }

    /**
     * Copies this DocumentParagaph, allowing safe sharing.
     *
     * Of course, we all know the only "safe" sharing is no sharing
     * at all, but this method should at least make sharing "safer".
     *
     * @return a deep copy of this DocumentParagraph, so that this !=
     *         this.copy(), this.getContents()!=this.copy().getContents(),
     *         et cetera, though though this.copy() is logically and
     *         functionally equivalent.
     */
    public DocumentParagraph copy() {

        UserName lockHolderCopy = null;
        if (this.lockHolder != null) {
            lockHolderCopy = new UserName(this.lockHolder.toString());
        }

        return new DocumentParagraph(this.contents.toString(),
                this.headerLevel, lockHolderCopy,
                new ParagraphIdentifier(this.id),
                this.differences.copy());

    }

    public void clear() {

        contents = new StringBuffer();
        differences = new DocumentParagraphDiff();
    }

    /**
     * Copies this paragaph's change log, resetting the internal copy
     * for later use.
     *
     * @return a DocumentParagraphDiff holding changes to this
     *         paragraph since the last {@link #getDifferences()}
     *         or {@link #resetDifferences()}
     */
    public DocumentParagraphDiff getDifferences() {

        DocumentParagraphDiff ret = differences.copy();

        resetDifferences();

        return ret;

    }

    /**
     * Copies this paragaph's change log, without resetting the internal log.
     *
     * @return a DocumentParagraphDiff holding changes to this paragraph since
     *         the last {@link #getDifferences()} or {@link #resetDifferences()}
     */
    public DocumentParagraphDiff peekDifferences() {
        return differences.copy();
    }

    /**
     * Clears this paragraph's log of changes.
     */
    public void resetDifferences() {
        differences.reset();
    }

    /**
     * Reports whether this paragaph has been changed since
     * its differences were last reset.
     *
     * @return a boolean
     */
    public boolean hasChanged() {
        return differences.hasChanges();
    }

    /** {@inheritDoc} */
    public ParagraphIdentifier getId() {
        return id;
    }

    /**
     * Set's the paragraph's id.
     *
     * @param id the id to set
     */
    public void setId(final ParagraphIdentifier id) {
        this.id = id;
    }

    /**
     * @param listener a listener to add
     */
    public void addParagraphListener(final ParagraphListener listener) {
        listeners.add(listener);
    }

    /**
     * @param listener a listener to remove
     */
    public void removeParagraphListener(final ParagraphListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all listeners that a user has a lock on this paragraph.
     *
     * @param newOwner the user with the lock
     */
    protected void fireOnLock(final UserName newOwner) {
        for (ParagraphListener listener : listeners) {
            listener.onLock(newOwner);
        }
    }

    /**
     * Notifies all listeners that no user has a lock on this paragraph.
     */
    protected void fireOnUnlock() {
        for (ParagraphListener listener : listeners) {
            listener.onUnlock();
        }
    }

    /**
     * Notifies all listeners that the header level has changed.
     *
     * @param headerLevel the new header level
     */
    protected void fireHeaderChange(final int headerLevel) {
        for (ParagraphListener listener : listeners) {
            listener.onHeaderChange(headerLevel);
        }
    }

    /**
     * Notifies all listeners that text has been inserted into this paragraph.
     *
     * @param offset the insertion position
     * @param hunk the text which was inserted
     */
    protected void fireOnInsert(final int offset, final String hunk) {
        for (ParagraphListener listener : listeners) {
            listener.onInsert(offset, hunk);
        }
    }

    /**
     * Notifies all listeners that text has been removed from this paragraph.
     *
     * @param offset the beginning index of the delete
     * @param length the number of deleted characters
     */
    protected void fireOnDelete(final int offset, final int length) {
        for (ParagraphListener listener : listeners) {
            listener.onDelete(offset, length);
        }
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Paragraph";
    }

    /** {@inheritDoc} */
    public XmlNode toXml() {
        XmlNode node = new XmlNode(xmlNodeName());
        node.setAttribute("id", this.id.getValue().toString());
        node.setContent(this.contents.toString());
        return node;
    }

    /** {@inheritDoc} */
    public void fromXml(final XmlNode node) throws XmlParseException {

        try {
            this.id = new ParagraphIdentifier(
                    Integer.parseInt(node.getAttribute("id")));
        } catch (final NumberFormatException e) {
            throw new XmlParseException(e);
        }

        this.contents.setLength(0);
        this.contents.append(node.getBody());

    }

    /**
     * @return this paragraph, formatted as an html entity
     */
    public String toHtml() {

        StringBuilder str = new StringBuilder();

        str.append("<p class=\"level" + headerLevel + "\">");
        str.append(formatForHtml(contents.toString()));
        str.append("</p>");

        return str.toString();

    }

    private static String formatForHtml(final String str) {
        return str.replaceAll("&", "&amp;")
                  .replaceAll("<", "&lt;")
                  .replaceAll(">", "&gt;")
                  .replaceAll("\n", "<br/>");
    }

}
