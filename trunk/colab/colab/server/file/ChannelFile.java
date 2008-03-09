package colab.server.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDataSet;
import colab.common.channel.ChannelDataStore;
import colab.common.util.FileUtils;

public class ChannelFile<T extends ChannelData>
        implements ChannelDataStore<T> {

    private final ChannelDataSet<T> dataCollection;

    private final File file;

    public ChannelFile(final File file) throws IOException {

        this.file = file;
        this.dataCollection = new ChannelDataSet<T>();

    }

    public void add(final T data) {

        dataCollection.add(data);

        String xmlString = data.toXml().serialize();
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

}
