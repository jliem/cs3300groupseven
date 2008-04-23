package colab.client;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

import colab.common.DebugManager;
import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDataSet;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.document.DocumentChannelData;
import colab.common.channel.document.EditDocChannelData;
import colab.common.channel.type.WhiteboardChannelType;
import colab.common.channel.whiteboard.EditLayer;
import colab.common.channel.whiteboard.Whiteboard;
import colab.common.channel.whiteboard.WhiteboardChannelData;
import colab.common.exception.NotApplicableException;
import colab.common.naming.ChannelName;

public class ClientWhiteboardChannel extends ClientChannel {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private Whiteboard whiteboard;
    private ChannelDataSet<WhiteboardChannelData> revisions;

    public ClientWhiteboardChannel(final ChannelName name)
            throws RemoteException {

        super(name);

        whiteboard = new Whiteboard();
        revisions = new ChannelDataSet<WhiteboardChannelData>();
    }

    public Whiteboard getWhiteboard() {
        return whiteboard;
    }

    public ChannelDataSet getChannelData() {
        return revisions;
    }

    public ChannelDescriptor getChannelDescriptor() {
        return new ChannelDescriptor(this.getId(), new WhiteboardChannelType());
    }

    public void add(final ChannelData data) throws RemoteException {

        DebugManager.debug("New channel data added " + data.toString());

        revisions.add((WhiteboardChannelData) data);

        try {
            ((WhiteboardChannelData) data).apply(whiteboard);
        } catch (NotApplicableException e) {
            DebugManager.shouldNotHappen(e);
        }

        ActionEvent event = new ActionEvent(
                this, ActionEvent.ACTION_FIRST, "Message Added");
        fireActionPerformed(event);

    }

    public void export(File file) throws IOException {
        // TODO Bug 83 - Exporting whiteboard image
    }

}
