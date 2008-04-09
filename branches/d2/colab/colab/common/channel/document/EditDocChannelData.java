package colab.common.channel.document;

import java.util.Date;

import colab.common.DebugManager;
import colab.common.channel.document.diff.DocumentParagraphDiff;
import colab.common.exception.NotApplicableException;
import colab.common.identity.ParagraphIdentifier;
import colab.common.naming.UserName;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;

public final class EditDocChannelData extends DocumentChannelData {

    /** Serialization version number. */
    private static final long serialVersionUID = 1L;

    private DocumentParagraphDiff differences;

    private ParagraphIdentifier paragraphID;

    public EditDocChannelData() {
    }

    public EditDocChannelData(final ParagraphIdentifier paragraphID,
            final DocumentParagraphDiff differences,
            final UserName creator, final Date timestamp) {
        super(creator, timestamp, DocumentChannelDataType.EDIT);

        this.differences = differences;
        this.paragraphID = paragraphID;
    }

    @Override
    public void apply(final Document doc) throws NotApplicableException {
        doc.applyEdit(paragraphID, differences);

        DocumentParagraph para = doc.get(paragraphID);
        DebugManager.debug("EditDocChanData says paragraph contents: "
                + para.getContents() + ", id is " + paragraphID
                + ", hashcode is " + para.hashCode());
    }

    public void apply(final DocumentParagraph para)
            throws NotApplicableException {
        if (!getCreator().equals(para.getLockHolder())) {
            throw new NotApplicableException("Editor does not hold lock.");
        }
        differences.apply(para);
    }

    public DocumentParagraphDiff getDifferences() {
        return differences;
    }

    public void setDifferences(final DocumentParagraphDiff differences) {
        this.differences = differences;
    }

    public ParagraphIdentifier getParagraphID() {
        return paragraphID;
    }

    public void setParagraphID(final ParagraphIdentifier paragraphID) {
        this.paragraphID = paragraphID;
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Edit";
    }

    /** {@inheritDoc} */
    public XmlNode toXml() {

        XmlNode node = super.toXml();

        node.setAttribute("paragraphId", this.paragraphID.toString());

        node.addChild(this.differences.toXml());

        return node;

    }

    /** {@inheritDoc} */
    public void fromXml(final XmlNode node) throws XmlParseException {

        super.fromXml(node);

        try {
            this.paragraphID = new ParagraphIdentifier(
                    Integer.parseInt(node.getAttribute("paragraphId")));
        } catch (final NumberFormatException e) {
            throw new XmlParseException(e);
        }

        this.differences = new DocumentParagraphDiff();
        this.differences.fromXml(node);

    }

}
