package colab.client;

import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.List;

import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.ChannelType;
import colab.common.channel.DocumentChannelData;
import colab.common.channel.DocumentDataSet;
import colab.common.naming.ChannelName;

public class ClientDocumentChannel extends ClientChannel {

    private static final long serialVersionUID = 1;
    
    private int newRevisions = 0;

    protected DocumentDataSet revisions;
    
    public ClientDocumentChannel(final ChannelName name) throws RemoteException{
        super(name);
        revisions = new DocumentDataSet();
    }

    public void add(ChannelData data) throws RemoteException {
        revisions.add((DocumentChannelData)data);
        newRevisions++;
        fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_FIRST,
                "Message Added"));
    }

    public ChannelDescriptor getChannelDescriptor() {
        return new ChannelDescriptor(this.getId(), ChannelType.DOCUMENT);
    }

    public List<DocumentChannelData> getLocalMessages() {
        return revisions.getLast(-1);
    }

    public int getLocalNumMessages() {
        return revisions.size();
    }

    public List<DocumentChannelData> getNewMessages() {
        List <DocumentChannelData> list = revisions.getLast(newRevisions);
        newRevisions = 0;
        return list;
    }

}
