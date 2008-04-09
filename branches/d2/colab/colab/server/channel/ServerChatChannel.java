package colab.server.channel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import colab.common.channel.ChannelDataSet;
import colab.common.channel.ChannelDataStore;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.chat.ChatChannelData;
import colab.common.channel.type.ChatChannelType;
import colab.common.naming.ChannelName;
import colab.server.file.ChannelFile;

/**
 * ServerChatChannel is a type of {@link ServerChannel} which
 * deals with chat channels.
 */
public final class ServerChatChannel extends ServerChannel<ChatChannelData> {

    /** Serialization version number. */
    private static final long serialVersionUID = 1L;

    /** The channel data. */
    private ChannelDataStore<ChatChannelData> messages;

    /**
     * Constructs a new server-side chat channel.
     *
     * @param name the name of the channel
     */
    public ServerChatChannel(final ChannelName name) {

        super(name);
        this.messages = new ChannelDataSet<ChatChannelData>();

    }

    /**
     * Constructs a new server-side chat channel.
     *
     * @param name the name of the channel
     * @param file the file to use for data storage
     * @throws IOException if a file storage error occurs
     */
    public ServerChatChannel(final ChannelName name, final File file)
            throws IOException {

        super(name);
        ChannelFile<ChatChannelData> channelFile =
            new ChannelFile<ChatChannelData>(
                file, ChatChannelData.getXmlConstructor());
        this.messages = channelFile;

    }

    /** {@inheritDoc} */
    public void add(final ChatChannelData data) {

        // Store the data, and assign it an identifier.
        messages.addAndAssignId(data);

        // Forward it to clients
        sendToAll(data);

    }

    /** {@inheritDoc} */
    public List<ChatChannelData> getLastData(final int count) {

        List<ChatChannelData> list = new ArrayList<ChatChannelData>();
        for(ChatChannelData ccd : messages.getLast(count)) {
            list.add(ccd);
        }
        return list;
    }

    /** {@inheritDoc} */
    public ChannelDescriptor getChannelDescriptor() {
        return new ChannelDescriptor(this.getId(), new ChatChannelType());
    }

}
