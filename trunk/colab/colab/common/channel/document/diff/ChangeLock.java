package colab.common.channel.document.diff;

import colab.common.channel.document.DocumentParagraph;
import colab.common.naming.InvalidUserNameException;
import colab.common.naming.UserName;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;

class ChangeLock implements Applicable {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private UserName newOwner;

    public ChangeLock() {
    }

    public ChangeLock(final UserName newOwner) {
        super();
        this.newOwner = newOwner;
    }

    public void apply(final DocumentParagraph para) {
        if (newOwner==null) {
            para.unlock();
        } else {
            para.lock(newOwner);
        }
    }

    public UserName getNewOwner() {
        return newOwner;
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "ChangeLock";
    }

    /** {@inheritDoc} */
    public XmlNode toXml() {

        XmlNode node = new XmlNode(xmlNodeName());
        node.setContent(this.newOwner.getValue());
        return node;

    }

    /** {@inheritDoc} */
    public void fromXml(final XmlNode node) throws XmlParseException {

        try {
            this.newOwner = new UserName(node.getBody());
        } catch (final InvalidUserNameException e) {
            throw new XmlParseException(e);
        }

    }

}
