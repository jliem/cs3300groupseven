package colab.server.channel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import colab.common.DebugManager;
import colab.common.channel.ChannelDataSet;
import colab.common.channel.ChannelDataStore;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.document.LockDocChannelData;
import colab.common.channel.type.WhiteboardChannelType;
import colab.common.channel.whiteboard.InsertLayer;
import colab.common.channel.whiteboard.Whiteboard;
import colab.common.channel.whiteboard.WhiteboardChannelData;
import colab.common.channel.whiteboard.layer.Layer;
import colab.common.channel.whiteboard.layer.LayerIdentifier;
import colab.common.exception.NotApplicableException;
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
        // TODO finish this - XML
    }

    public void add(final WhiteboardChannelData data) {

        DebugManager.debug("Server received " + data);

        // If this is an insert, set the paragraph id
        if (data instanceof InsertLayer) {

            InsertLayer insertData = ((InsertLayer)data);

            revisions.addAndAssignId(data);

            // If we don't have a layer already, create it
            if (insertData.getLayer() == null) {
                LayerIdentifier layerId =
                    new LayerIdentifier(data.getId());

                // Create a blank layer with no lock holder
                Layer layer =
                    new Layer(layerId);
                layer.setId(layerId);

                insertData.setLayer(layer);
            }
        }

        DebugManager.debug(" # Still adding");

        try {

            // Check for channel data validity
            // SHOULD take care of bad locks, et cetera
            data.apply(currentBoard.copy());

            DebugManager.debug("Server is adding data: " + data.toString());

            data.apply(currentBoard);

        } catch (final NotApplicableException ex) {

            // If the apply didn't work, remove the revision
            // if we added it

            // TODO The code below doesn't work because it uses the
            // compareTo method in Identifier, which crashes when
            // the value is null

//            if (revisions.contains(data)) {
//                revisions.remove(data);
//            }

            return;
        }

        DebugManager.debug(" # Applied");

        // Store the data, and assign it an identifier
        // Might have already added this if it's an
        // InsertDocChannelData but that's ok
        if (!(data instanceof InsertLayer)) {
            revisions.addAndAssignId(data);
        }

        DebugManager.debug(" # Stored");

        // Forward it to all clients, regardless of the creator
        (new Thread() {
            public void run() {
                sendToAllRegardless(data);
            }
        }).start();

        DebugManager.debug(" # Sent");

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
