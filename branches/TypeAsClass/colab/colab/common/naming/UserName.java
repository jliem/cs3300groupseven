package colab.common.naming;

import java.text.ParseException;

import colab.common.identity.StringIdentifier;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlSerializable;

/**
 * The name a user uses to log in.
 */
public class UserName extends StringIdentifier implements XmlSerializable {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

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
    public final XmlNode toXml() {
        XmlNode node = new XmlNode("UserName");
        node.setContent(getValue());
        return node;
    }

    /**
     * Constructs a new Community from the data in an XmlNode.
     *
     * @param node the xml node containing data
     * @return a Community built from the data in the xml node
     * @throws ParseException if the data is improperly formatted
     */
    public static UserName fromXml(final XmlNode node) throws ParseException {
        String name = node.getBody();
        return new UserName(name);
    }

}
