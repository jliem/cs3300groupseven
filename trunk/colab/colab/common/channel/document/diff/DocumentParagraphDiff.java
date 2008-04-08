package colab.common.channel.document.diff;

import java.util.List;
import java.util.Vector;

import colab.common.DebugManager;
import colab.common.channel.document.DocumentParagraph;
import colab.common.exception.NotApplicableException;
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
        changes = new Vector<Applicable>();
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
        List<Applicable> list = new Vector<Applicable>(changes);
        return new DocumentParagraphDiff(list);
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Differences";
    }

    /** {@inheritDoc} */
    public XmlNode toXml() {

        XmlNode node = new XmlNode("Differences");

        for (Applicable change : changes) {
            node.addChild(change.toXml());
        }

        return node;

    }

    /** {@inheritDoc} */
    public void fromXml(final XmlNode node) throws XmlParseException {

        for (XmlNode child : node.getChildren()) {
            Applicable change = instantiateApplicableFromXml(child.getType());
            change.fromXml(child);
            changes.add(change);
        }

    }

    private static Applicable instantiateApplicableFromXml(
            final String type) throws XmlParseException {

        Applicable applicable;

        applicable = new ChangeLevel();
        if (applicable.xmlNodeName().equals(type)) {
            return applicable;
        }

        applicable = new Delete();
        if (applicable.xmlNodeName().equals(type)) {
            return applicable;
        }

        applicable = new Insert();
        if (applicable.xmlNodeName().equals(type)) {
            return applicable;
        }

        throw new XmlParseException();

    }

}
