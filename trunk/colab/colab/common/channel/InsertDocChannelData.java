package colab.common.channel;

import java.util.Date;

import colab.common.Document;
import colab.common.DocumentParagraph;
import colab.common.naming.UserName;
import colab.common.xml.XmlNode;

public final class InsertDocChannelData extends DocumentChannelData {

    /** Serialization version number. */
    private static final long serialVersionUID = 1;

    private int offset;

    private DocumentParagraph paragraph;

    public InsertDocChannelData(final int offset,
            final DocumentParagraph paragraph,
            final UserName creator, final Date timestamp) {

        super(creator, timestamp, DocumentChannelDataType.INSERT);

        this.offset = offset;
        this.paragraph = paragraph;

    }

    @Override
    public void apply(final Document doc) throws Exception {
        if(offset > doc.getNumberParagraphs()) {
            throw new Exception("Offset outside of document limits.");
        }
        doc.insert(offset, paragraph);
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(final int offset) {
        this.offset = offset;
    }

    public DocumentParagraph getParagraph() {
        return paragraph;
    }

    public void setParagraph(final DocumentParagraph paragraph) {
        this.paragraph = paragraph;
    }

    /** {@inheritDoc} */
    public XmlNode toXml() {
        XmlNode node = new XmlNode("Insert");
        node.setAttribute("offset", Integer.toString(offset));
        node.setContent(differences.toXml());
        return node;
    }

}
