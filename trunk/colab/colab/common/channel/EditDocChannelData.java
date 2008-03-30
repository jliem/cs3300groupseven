package colab.common.channel;

import java.util.Date;

import colab.common.Document;
import colab.common.DocumentParagraph;
import colab.common.DocumentParagraphDiff;
import colab.common.identity.ParagraphIdentifier;
import colab.common.naming.UserName;
import colab.common.xml.XmlNode;

public final class EditDocChannelData extends DocumentChannelData {

    /** Serialization version number. */
    private static final long serialVersionUID = 1L;

    private DocumentParagraphDiff differences;

    private ParagraphIdentifier paragraphId;

    public EditDocChannelData(final ParagraphIdentifier paragraphId,
            final DocumentParagraphDiff differences,
            final UserName creator, final Date timestamp) {
        super(creator, timestamp, DocumentChannelDataType.EDIT);

        this.differences = differences;
        this.paragraphId = paragraphId;
    }

    @Override
    public void apply(final Document doc) throws Exception {
        doc.applyEdit(paragraphId, differences);
    }

    public void apply(final DocumentParagraph para) {
        differences.apply(para);
    }

    public DocumentParagraphDiff getDifferences() {
        return differences;
    }

    public void setDifferences(final DocumentParagraphDiff differences) {
        this.differences = differences;
    }

    public ParagraphIdentifier getParagraphId() {
        return paragraphId;
    }

    public void setParagraphID(final ParagraphIdentifier paragraphId) {
        this.paragraphId = paragraphId;
    }

    /** {@inheritDoc} */
    public XmlNode toXml() {
        XmlNode node = new XmlNode("Edit");
        node.setAttribute("paragraphId", paragraphId.toString());
        node.setContent(differences.toXml());
        return node;
    }

}
