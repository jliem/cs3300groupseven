package colab.server.channel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import colab.common.DebugManager;
import colab.common.channel.ChannelDataSet;
import colab.common.channel.ChannelDataStore;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.type.WhiteboardChannelType;
import colab.common.channel.whiteboard.InsertLayer;
import colab.common.channel.whiteboard.Whiteboard;
import colab.common.channel.whiteboard.WhiteboardChannelData;
import colab.common.channel.whiteboard.layer.Layer;
import colab.common.channel.whiteboard.layer.LayerIdentifier;
import colab.common.exception.NotApplicableException;
import colab.common.naming.ChannelName;
import colab.server.file.ChannelFile;

/**
 * ServerDocumentChannel is a type of {@link ServerChannel} which
 * deals with document channels.
 */
public final class ServerWhiteboardChannel
        extends ServerChannel<WhiteboardChannelData> {

    private ChannelDataStore<WhiteboardChannelData> revisions;

    private Whiteboard currentBoard;

    /**
     * Constructs a new server-side whiteboard channel.
     *
     * @param name the name of the channel
     */
    public ServerWhiteboardChannel(final ChannelName name) {
        super(name);

        this.revisions = new ChannelDataSet<WhiteboardChannelData>();

        this.currentBoard = new Whiteboard();
    }

    /**
     * Constructs a new server-side whiteboard channel.
     *
     * @param name the name of the channel
     * @param file the file to use for data storage
     * @throws IOException if a file storage error occurs
     */
    public ServerWhiteboardChannel(final ChannelName name, final File file)
            throws IOException {

        super(name);

        ChannelFile<WhiteboardChannelData> channelFile =
            new ChannelFile<WhiteboardChannelData>(
                file, WhiteboardChannelData.getXmlConstructor());
        this.revisions = channelFile;

        this.currentBoard = new Whiteboard();
        for (WhiteboardChannelData data : revisions.getAll()) {
            try {
                data.apply(currentBoard);
            } catch (final NotApplicableException e) {
                DebugManager.exception(e);
            }
        }

    }

    /** {@inheritDoc} */
    @Override
    public void add(final WhiteboardChannelData data) {

        try {

            // Check for channel data validity
            // SHOULD take care of bad locks, et cetera
            data.apply(currentBoard.copy());

        } catch (final NotApplicableException e) {
            DebugManager.exception(e);
            return;
        }

        // If this is an insert, set the layer id
        if (data instanceof InsertLayer) {

            InsertLayer insertData = ((InsertLayer) data);

            revisions.addAndAssignId(data);

            LayerIdentifier layerId = new LayerIdentifier(data.getId());

            insertData.getLayer().setId(layerId);

        }

        try {
            data.apply(currentBoard);
        } catch (NotApplicableException e) {
            DebugManager.shouldNotHappen(e);
        }

        // Store the data, and assign it an identifier
        // Might have already added this if it's an
        // InsertDocChannelData but that's ok
        if (!(data instanceof InsertLayer)) {
            revisions.addAndAssignId(data);
        }

        // Forward it to all clients, regardless of the creator
        (new Thread() {
            public void run() {
                sendToAllRegardless(data);
            }
        }).start();

    }

    /** {@inheritDoc} */
    @Override
    public ChannelDescriptor getChannelDescriptor() {
        return new ChannelDescriptor(this.getId(), new WhiteboardChannelType());
    }

    /** {@inheritDoc} */
    @Override
    public List<WhiteboardChannelData> getLastData(final int count) {
        List<WhiteboardChannelData> list =
            new ArrayList<WhiteboardChannelData>();
        for(WhiteboardChannelData wcd : revisions.getLast(count)) {
            list.add(wcd);
        }
        return list;
    }

}
