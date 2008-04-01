package colab.client;

import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.List;

import colab.common.DebugManager;
import colab.common.Document;
import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDataSet;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.DocumentChannelData;
import colab.common.channel.type.DocumentChannelType;
import colab.common.exception.NotApplicableException;
import colab.common.naming.ChannelName;

public final class ClientDocumentChannel extends ClientChannel {

    /** Serialization version number. */
    private static final long serialVersionUID = 1;

    private int newRevisions = 0;

    private ChannelDataSet<DocumentChannelData> revisions;

    private Document currentDocument;

    public ClientDocumentChannel(final ChannelName name) throws RemoteException {

        super(name);
        revisions = new ChannelDataSet<DocumentChannelData>();
        currentDocument = new Document();
    }

    /** {@inheritDoc} */
    public void add(final ChannelData data) throws RemoteException {

        revisions.add((DocumentChannelData) data);
        newRevisions++;

        try {
            ((DocumentChannelData) data).apply(currentDocument);
        } catch (NotApplicableException e) {
            DebugManager.shouldNotHappen(e);
        }

        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_FIRST, "Message Added");
        fireActionPerformed(event);

    }

    /** {@inheritDoc} */
    public ChannelDescriptor getChannelDescriptor() {

        return new ChannelDescriptor(this.getId(), new DocumentChannelType());

    }

    public List<DocumentChannelData> getLocalMessages() {

        return revisions.getLast(-1);

    }

    public int getLocalNumMessages() {

        return revisions.size();

    }

    public List<DocumentChannelData> getNewMessages() {
        List<DocumentChannelData> list = revisions.getLast(newRevisions);
        newRevisions = 0;
        return list;
    }

    public Document getCurrentDocument() {
        return currentDocument;
    }

    public ChannelDataSet<DocumentChannelData> getChannelData() {
        return revisions;
    }

}
