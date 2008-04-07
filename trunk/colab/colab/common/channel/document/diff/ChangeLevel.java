package colab.common.channel.document.diff;

import colab.common.channel.document.DocumentParagraph;
import colab.common.exception.NotApplicableException;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;

class ChangeLevel implements Applicable {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private int headerLevel;

    public ChangeLevel() {
    }

    public ChangeLevel(final int headerLevel) {
        super();
        this.headerLevel = headerLevel;
    }

    public void apply(final DocumentParagraph para)
            throws NotApplicableException {
        if (headerLevel<0) {
            para.setHeaderLevel(headerLevel);
        }
    }

    public int getHeaderLevel() {
        return headerLevel;
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "ChangeLevel";
    }

    /** {@inheritDoc} */
    public XmlNode toXml() {

        XmlNode node = new XmlNode(xmlNodeName());
        node.setContent(Integer.toString(this.headerLevel));
        return node;

    }

    /** {@inheritDoc} */
    public void fromXml(final XmlNode node) throws XmlParseException {

        try {
            this.headerLevel = Integer.parseInt(node.getBody());
        } catch (final NumberFormatException e) {
            throw new XmlParseException(e);
        }

    }

}
