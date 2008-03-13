package colab.server.file;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDataIdentifier;
import colab.common.channel.ChannelDataSet;
import colab.common.channel.ChannelDataStore;
import colab.common.util.FileUtils;
import colab.common.xml.XmlConstructor;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlReader;

public class ChannelFile<T extends ChannelData>
        implements ChannelDataStore<T> {

    private final ChannelDataSet<T> dataCollection;

    private final File file;

    public ChannelFile(final File file,
            final XmlConstructor<T> constructor)
            throws IOException {

        this.file = file;
        this.dataCollection = new ChannelDataSet<T>();

        final XmlReader xmlReader = new XmlReader(file);
        final List<XmlNode> xml = xmlReader.getXml();
        for (final XmlNode node : xml) {
            T data;
            try {
                data = constructor.fromXml(node);
            } catch (final ParseException e) {
                throw new IOException(e.getMessage());
            }
            dataCollection.add(data);
        }

    }

    public void add(final T data) {

        dataCollection.add(data);

        XmlNode xml = data.toXml();
        String xmlString = xml.serialize();
        try {
            FileUtils.appendLine(file, xmlString);
        } catch (final IOException ioe) {
            ioe.printStackTrace();
        }

    }

    public List<T> getAll() {

        return dataCollection.getAll();

    }

    public List<T> getLast(final int count) {

        return dataCollection.getLast(count);

    }

    public ChannelDataIdentifier getNextDataId() {
        return dataCollection.getNextDataId();
    }

}
