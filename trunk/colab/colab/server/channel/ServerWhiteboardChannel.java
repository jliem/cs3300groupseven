package colab.server.channel;

import java.io.File;
import java.io.IOException;
import java.util.List;

import colab.common.channel.ChannelDescriptor;
import colab.common.channel.type.WhiteboardChannelType;
import colab.common.channel.whiteboard.WhiteboardChannelData;
import colab.common.naming.ChannelName;

/**
 * ServerDocumentChannel is a type of {@link ServerChannel} which
 * deals with document channels.
 */
public final class ServerWhiteboardChannel
        extends ServerChannel<WhiteboardChannelData> {

    public ServerWhiteboardChannel(ChannelName name) {
        super(name);
        // TODO Auto-generated constructor stub
    }

    public ServerWhiteboardChannel(final ChannelName name, final File file)
    throws IOException {
        super(name);
        // TODO finish this
    }

    public void add(WhiteboardChannelData data) {
        // TODO Auto-generated method stub

    }

    public ChannelDescriptor getChannelDescriptor() {
        return new ChannelDescriptor(this.getId(), new WhiteboardChannelType());
    }

    public List<WhiteboardChannelData> getLastData(int count) {
        // TODO Auto-generated method stub
        return null;
    }

}
