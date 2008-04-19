package colab.common.channel.document.diff;

import java.io.Serializable;
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
public final class DocumentParagraphDiff
        implements XmlSerializable, Serializable {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private List<Applicable> changes;

    public DocumentParagraphDiff() {
        changes = new Vector<Applicable>();
    }

    protected DocumentParagraphDiff(final List<Applicable> changes) {
        this.changes = changes;
    }

    public void apply(final DocumentParagraph paragraph)
            throws NotApplicableException {

        DebugManager.debug(" @ copying");

        DocumentParagraph ret = paragraph.copy();

        DebugManager.debug(" @ trying " + changes.size());

        for (Applicable change : changes) {
            DebugManager.debug(" @ -");
            try {
                change.apply(ret);
                DebugManager.debug(" @ #");
            } catch (Exception e) {
                throw new NotApplicableException(e);
            }
        }

        DebugManager.debug(" @ now for real");

        // All changes were successful, apply to real paragraph
        for (Applicable change : changes) {
            try {
                DebugManager.debug(" @ -");
                change.apply(paragraph);
                DebugManager.debug(" @ #");
            } catch (Exception e) {
                throw new NotApplicableException(e);
            }
        }


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

        for (XmlNode child : node.getChildren().get(0).getChildren()) {
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

        DebugManager.debug("Type " + type + " not recognized");
        throw new XmlParseException();

    }

}
