package colab.common.channel.document.diff;

import colab.common.channel.document.DocumentParagraph;
import colab.common.exception.NotApplicableException;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;

class Delete implements Applicable {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private int offset;

    private int length;

    public Delete() {
    }

    public Delete(final int offset, final int length) {
        super();
        this.offset = offset;
        this.length = length;
    }

    public void apply(final DocumentParagraph para)
            throws NotApplicableException {
        if (offset + length > para.getLength()) {
            throw new NotApplicableException(
                    "Delete not in range of paragraph.");
        } else {
            para.delete(offset, length);
        }
    }

    public int getLength() {
        return length;
    }

    public int getOffset() {
        return offset;
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Delete";
    }

    /** {@inheritDoc} */
    public XmlNode toXml() {

        XmlNode node = new XmlNode(xmlNodeName());
        node.setAttribute("offset", Integer.toString(this.offset));
        node.setAttribute("length", Integer.toString(this.length));
        return node;

    }

    /** {@inheritDoc} */
    public void fromXml(final XmlNode node) throws XmlParseException {

        try {
            this.offset = Integer.parseInt(node.getAttribute("offset"));
        } catch (final NumberFormatException e) {
            throw new XmlParseException(e);
        }

        try {
            this.length = Integer.parseInt(node.getAttribute("length"));
        } catch (final NumberFormatException e) {
            throw new XmlParseException(e);
        }

    }

}
