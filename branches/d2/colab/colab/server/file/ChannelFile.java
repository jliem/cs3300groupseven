package colab.server.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

import colab.common.DebugManager;
import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDataSet;
import colab.common.channel.ChannelDataStore;
import colab.common.util.FileUtils;
import colab.common.xml.XmlConstructor;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;
import colab.common.xml.XmlReader;

/**
 * A collection of data in a channel, backed by a
 * {@link ChannelDataSet} and maintained persistently
 * by a text file.
 *
 * @param <T> the type of channel data in the collection
 */
public final class ChannelFile<T extends ChannelData>
        implements ChannelDataStore<T> {

    /** The backing data set. */
    private final ChannelDataSet<T> dataCollection;

    /** The file used for persistent storage. */
    private final File file;

    /**
     * Construct a new ChannelFile.
     *
     * @param file the file to use for persistent storage
     * @param constructor a closure used to construct data elements from xml
     * @throws IOException if an I/O error occurs
     */
    public ChannelFile(final File file,
            final XmlConstructor<T> constructor)
            throws IOException {

        this.file = file;
        this.dataCollection = new ChannelDataSet<T>();

        final XmlReader xmlReader = new XmlReader(file);
        try {
            final List<XmlNode> xml = xmlReader.getXml();
            for (final XmlNode node : xml) {
                T data;
                data = constructor.fromXml(node);
                dataCollection.add(data);
            }
        } catch (final XmlParseException e) {
            throw new IOException(e.getMessage());
        }

    }

    /** {@inheritDoc} */
    public void add(final T data) {

        dataCollection.add(data);

        XmlNode xml = data.toXml();
        String xmlString = xml.serialize();
        try {
            FileUtils.appendLine(file, xmlString);
        } catch (final IOException ioe) {
            DebugManager.ioException(ioe);
        }

    }

    /** {@inheritDoc} */
    public void addAndAssignId(final T data) {
        data.setId(null);
        add(data);
    }

    /** {@inheritDoc} */
    public List<T> getAll() {

        return dataCollection.getAll();

    }

    /** {@inheritDoc} */
    public List<T> getLast(final int count) {

        return dataCollection.getLast(count);

    }

    /** {@inheritDoc} */
    public int size() {

        return dataCollection.size();

    }

}
