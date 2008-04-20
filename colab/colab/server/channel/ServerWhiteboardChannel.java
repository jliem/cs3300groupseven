package colab.server.channel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import colab.common.channel.ChannelDataSet;
import colab.common.channel.ChannelDataStore;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.type.WhiteboardChannelType;
import colab.common.channel.whiteboard.Whiteboard;
import colab.common.channel.whiteboard.WhiteboardChannelData;
import colab.common.naming.ChannelName;

/**
 * ServerDocumentChannel is a type of {@link ServerChannel} which
 * deals with document channels.
 */
public final class ServerWhiteboardChannel
        extends ServerChannel<WhiteboardChannelData> {

    private ChannelDataStore<WhiteboardChannelData> revisions;
    
    private Whiteboard currentBoard;
    
    public ServerWhiteboardChannel(final ChannelName name) {
        super(name);

        this.revisions = new ChannelDataSet<WhiteboardChannelData>();
        
        this.currentBoard = new Whiteboard();
    }

    public ServerWhiteboardChannel(final ChannelName name, final File file)
    throws IOException {
        super(name);
        // TODO finish this
    }

    public void add(final WhiteboardChannelData data) {
        // TODO Auto-generated method stub

    }

    public ChannelDescriptor getChannelDescriptor() {
        return new ChannelDescriptor(this.getId(), new WhiteboardChannelType());
    }

    /** {@inheritDoc} */
    @Override
    public List<WhiteboardChannelData> getLastData(final int count) {
        List<WhiteboardChannelData> list = new ArrayList<WhiteboardChannelData>();
        for(WhiteboardChannelData wcd : revisions.getLast(count)) {
            list.add(wcd);
        }
        return list;
    }
}
