package colab.common.naming;

import colab.common.identity.StringIdentifier;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;
import colab.common.xml.XmlSerializable;

/**
 * The name a user uses to log in.
 */
public class UserName extends StringIdentifier implements XmlSerializable {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * Constructs an empty (null-value) UserName.
     */
    public UserName() {
    }

    /**
     * Constructs a new user name.
     *
     * @param name the name of a user
     */
    public UserName(final String name) {
        super(name);
        if (!ColabNameRules.isValidUserName(name)) {
            throw new InvalidUserNameException();
        }
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "UserName";
    }

    /** {@inheritDoc} */
    public final XmlNode toXml() {
        XmlNode node = new XmlNode(xmlNodeName());
        node.setContent(getValue());
        return node;
    }

    /**
     * Populates the UserName from data in an XmlNode.
     *
     * @param node the xml node containing data
     * @throws XmlParseException if the data is improperly formatted
     */
    public void fromXml(final XmlNode node) throws XmlParseException {
        setValue(node.getBody());
    }

}
