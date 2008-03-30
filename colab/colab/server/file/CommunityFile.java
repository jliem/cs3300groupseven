package colab.server.file;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

import colab.common.DebugManager;
import colab.common.naming.CommunityName;
import colab.common.util.FileUtils;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlReader;
import colab.server.user.Community;
import colab.server.user.CommunitySet;
import colab.server.user.CommunityStore;

/**
 * A collection of files, backed by a {@link CommunitySet}
 * and maintained persistently by a text file.
 */
public final class CommunityFile implements CommunityStore {

    /** The backing community set. */
    private final CommunitySet communities;

    /** The file for persistent storage. */
    private final File file;

    /**
     * Contructs a new CommunityFile.
     *
     * @param file the file to use for persistent storage
     * @throws IOException if an I/O exception occurs
     */
    public CommunityFile(final File file) throws IOException {

        this.file = file;
        this.communities = new CommunitySet();

        final XmlReader xmlReader = new XmlReader(file);
        final List<XmlNode> xml = xmlReader.getXml();
        for (final XmlNode node : xml) {
            Community data;
            try {
                data = Community.fromXml(node);
            } catch (final ParseException e) {
                throw new IOException(e.getMessage());
            }
            communities.add(data);
        }

    }

    /** {@inheritDoc} */
    public Collection<Community> getAll() {
        return communities.getAll();
    }

    /** {@inheritDoc} */
    public void add(final Community community) {

        communities.add(community);

        String str = community.toXml().serialize();
        try {
            FileUtils.appendLine(file, str);
        } catch (final IOException ioe) {
            DebugManager.ioException(ioe);
        }

    }

    /** {@inheritDoc} */
    public Community get(final CommunityName name) {
        return communities.get(name);
    }

}
