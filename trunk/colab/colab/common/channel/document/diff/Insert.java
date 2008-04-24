package colab.common.channel.document.diff;

import colab.common.channel.document.DocumentParagraph;
import colab.common.exception.NotApplicableException;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;

public class Insert implements Applicable {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private String contents;

    private int offset;

    public Insert() {
    }

    public Insert(final int offset, final String contents) {
        super();
        this.contents = contents;
        this.offset = offset;
    }

    public void apply(final DocumentParagraph para)
            throws NotApplicableException {
        if (offset > para.getLength()) {
            throw new NotApplicableException(
                    "Insert outside of range of paragraph.");
        } else {

            para.insert(offset, contents);
        }
    }

    public String getContents() {
        return contents;
    }

    public int getOffset() {
        return offset;
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Insert";
    }


    /** {@inheritDoc} */
    public XmlNode toXml() {

        XmlNode node = new XmlNode(xmlNodeName());

        node.setContent(this.contents);
        node.setAttribute("offset", Integer.toString(this.offset));

        return node;

    }

    /** {@inheritDoc} */
    public void fromXml(final XmlNode node) throws XmlParseException {

        this.contents = node.getBody();

        try {
            this.offset = Integer.parseInt(node.getAttribute("offset"));
        } catch (final NumberFormatException e) {
            throw new XmlParseException(e);
        }

    }

}
