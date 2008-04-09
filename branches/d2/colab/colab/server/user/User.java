package colab.server.user;

import java.io.Serializable;

import colab.common.identity.Identifiable;
import colab.common.naming.UserName;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;
import colab.common.xml.XmlSerializable;

/**
 * Represents a user of the system.
 */
public final class User implements Identifiable<UserName>,
        Serializable, XmlSerializable {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * A unique string identifying this user.
     */
    private UserName name;

    /**
     * The password for this user to log in.
     */
    private Password pass;

    /**
     * Constructs an empty User.
     */
    public User() {
    }

    /**
     * Constructs a new User.
     *
     * @param name a unique string identifying this user
     * @param pass the password for this user to log in
     */
    public User(final UserName name, final Password pass) {
        this.name = name;
        this.pass = pass;
    }

    /**
     * Constructs a new User.
     *
     * @param name a unique string identifying this user
     * @param pass the password for this user to log in
     */
    public User(final String name, final char[] pass) {
        this(new UserName(name), new Password(pass));
    }

    /**
     * Sets the user's password.
     *
     * @param pass the new password for this user
     */
    public void setPassword(final Password pass) {
        this.pass = pass;
    }

    /**
     * @return the user's password
     */
    public Password getPassword() {
        return this.pass;
    }

    /**
     * Verifies whether a given password string is correct for this user.
     *
     * @param attempt an input string which may be a correct password
     * @return true if the given password is correct, false otherwise
     */
    public boolean checkPassword(final char[] attempt) {
        return pass.checkPassword(attempt);
    }

    /**
     * Returns the string which identifies this user.
     *
     * @return the name of this user
     */
    public UserName getId() {
        return name;
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "User";
    }

    /** {@inheritDoc} */
    public XmlNode toXml() {
        XmlNode node = new XmlNode(xmlNodeName());
        node.setAttribute("name", name.getValue());
        node.setAttribute("password", pass.getHash());
        return node;
    }

    /** {@inheritDoc} */
    public void fromXml(final XmlNode node) throws XmlParseException {
        this.name = new UserName(node.getAttribute("name"));
        this.pass = new Password(node.getAttribute("password"));
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "User: " + getId().toString() + ", pass: " + pass.getHash();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {

        if (!(object instanceof User)) {
            return false;
        }

        User otherUser = (User) object;

        return getId().equals(otherUser.getId())
            && getPassword().equals(otherUser.getPassword());

    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return getId().hashCode()
             + getPassword().hashCode();
    }

}
