package colab.common.channel.document.diff;

import java.util.ArrayList;
import java.util.List;

import colab.common.channel.document.DocumentParagraph;
import colab.common.exception.NotApplicableException;
import colab.common.naming.UserName;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;
import colab.common.xml.XmlSerializable;

/**
 * A log for paragaph inserts, deletes, and header changes.
 *
 * @see DocumentParagraph
 */
public final class DocumentParagraphDiff implements XmlSerializable {

    private List<Applicable> changes;

    public DocumentParagraphDiff() {
        changes = new ArrayList<Applicable>();
    }

    protected DocumentParagraphDiff(final List<Applicable> changes) {
        this.changes = changes;
    }

    public DocumentParagraph apply(final DocumentParagraph paragraph)
            throws NotApplicableException {

        DocumentParagraph ret = paragraph.copy();

        for (Applicable change : changes) {
            try {
                change.apply(ret);
            } catch (Exception e) {
                throw new NotApplicableException(e);
            }
        }

       return ret;

    }

    public void insert(final int offset, final String content) {
        changes.add(new Insert(offset, content));
    }

    public void delete(final int offset, final int length) {
        changes.add(new Delete(offset, length));
    }

    public void changeHeaderLevel(final int headerLevel) {
        changes.add(new ChangeLevel(headerLevel));
    }

    /**
     * @param newOwner the new owner of the lock,
     *                 specifed as {@code null} to unlock
     */
    public void lock(final UserName newOwner) {
        changes.add(new ChangeLock(newOwner));
    }

    public void unlock() {
        changes.add(new ChangeLock(null));
    }

    public void reset() {
        changes.clear();
    }

    public int getNumberChanges() {
        return changes.size();
    }

    public boolean hasChanges() {
        return changes.size() > 0;
    }

    public DocumentParagraphDiff copy() {
        List<Applicable> list = new ArrayList<Applicable>(changes);
        return new DocumentParagraphDiff(list);
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Differences";
    }

    /** {@inheritDoc} */
    public XmlNode toXml() {

        return null; // TODO:

    }

    /** {@inheritDoc} */
    public void fromXml(final XmlNode node) throws XmlParseException {

        // TODO:

    }

}
