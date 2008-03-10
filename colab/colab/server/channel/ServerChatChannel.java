package colab.server.channel;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDataStore;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.ChannelType;
import colab.common.channel.ChatChannelData;
import colab.common.channel.ChatDataSet;
import colab.common.naming.ChannelName;
import colab.server.file.ChannelFile;

public final class ServerChatChannel extends ServerChannel {

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
        this.messages = new ChatDataSet();

    }

    public ServerChatChannel(final ChannelName name, final File file)
            throws IOException {

        super(name);
        this.messages = new ChannelFile<ChatChannelData>(
                file, ChatChannelData.getXmlConstructor());

    }

    /** {@inheritDoc} */
    public void add(final ChannelData data) throws RemoteException {

        // Store the data
        messages.add((ChatChannelData) data);

        // Forward it to clients
        sendToAll(data);

    }

    /** {@inheritDoc} */
    public List<ChannelData> getLastData(final int count) {

        List<ChannelData> list = new ArrayList<ChannelData>();
        for(ChatChannelData ccd : messages.getLast(count)) {
            list.add(ccd);
        }
        return list;
    }

    public ChannelDescriptor getChannelDescriptor() {
        return new ChannelDescriptor(this.getId(), ChannelType.CHAT);
    }

}
