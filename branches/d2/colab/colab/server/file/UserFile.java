package colab.server.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

import colab.common.DebugManager;
import colab.common.naming.UserName;
import colab.common.util.FileUtils;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;
import colab.common.xml.XmlReader;
import colab.server.user.User;
import colab.server.user.UserSet;
import colab.server.user.UserStore;

/**
 * A collection of users, backed by a {@link UserSet}
 * and maintained persistently by a text file.
 */
public final class UserFile implements UserStore {

    /** The backing user set. */
    private final UserSet users;

    /** The file for persistent storage. */
    private final File file;

    /**
     * Contructs a new UserFile.
     *
     * @param file the file to use for persistent storage
     * @throws IOException if an I/O exception occurs
     */
    public UserFile(final File file) throws IOException {

        this.file = file;
        this.users = new UserSet();

        final XmlReader xmlReader = new XmlReader(file);
        try {
            final List<XmlNode> xml = xmlReader.getXml();
            for (final XmlNode node : xml) {
                User user = new User();
                user.fromXml(node);
                users.add(user);
            }
        } catch (final XmlParseException e) {
            throw new IOException(e.getMessage());
        }

    }

    /** {@inheritDoc} */
    public void add(final User user) {

        users.add(user);

        String str = user.toXml().serialize();
        try {
            FileUtils.appendLine(file, str);
        } catch (final IOException ioe) {
            DebugManager.ioException(ioe);
        }

    }

    /** {@inheritDoc} */
    public User get(final UserName name) {
        return users.get(name);
    }

}
