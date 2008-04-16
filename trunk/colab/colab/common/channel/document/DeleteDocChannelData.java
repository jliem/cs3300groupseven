package colab.common.channel.document;

import java.util.Date;

import colab.common.exception.NotApplicableException;
import colab.common.identity.ParagraphIdentifier;
import colab.common.naming.UserName;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;

public final class DeleteDocChannelData extends DocumentChannelData {

    /** Serialization version number. */
    private static final long serialVersionUID = 1L;

    private ParagraphIdentifier paragraphID;

    public DeleteDocChannelData() {
    }

    public DeleteDocChannelData(final ParagraphIdentifier paragraphID,
            final UserName creator, final Date timestamp) {

        super(creator, timestamp, DocumentChannelDataType.DELETE);

        this.paragraphID = paragraphID;

    }

    @Override
    public void apply(final Document doc) throws NotApplicableException {
        doc.delete(paragraphID);
    }

    public ParagraphIdentifier getParagraphID() {
        return paragraphID;
    }

    public void setParagraphID(final ParagraphIdentifier paragraphID) {
        this.paragraphID = paragraphID;
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Delete";
    }

    /** {@inheritDoc} */
    @Override
    public XmlNode toXml() {

        XmlNode node = super.toXml();

        node.setAttribute("paragraphId", this.paragraphID.toString());

        return node;

    }

    /** {@inheritDoc} */
    @Override
    public void fromXml(final XmlNode node) throws XmlParseException {

        super.fromXml(node);

        try {
            this.paragraphID = new ParagraphIdentifier(
                    Integer.parseInt(node.getAttribute("paragraphId")));
        } catch (final NumberFormatException e) {
            throw new XmlParseException(e);
        }

    }


}
