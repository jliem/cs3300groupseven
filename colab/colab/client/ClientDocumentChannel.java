package colab.client;

import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.List;

import colab.common.DebugManager;
import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDataSet;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.document.Document;
import colab.common.channel.document.DocumentChannelData;
import colab.common.channel.document.LockDocChannelData;
import colab.common.channel.type.DocumentChannelType;
import colab.common.exception.NotApplicableException;
import colab.common.identity.ParagraphIdentifier;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;

public final class ClientDocumentChannel extends ClientChannel {

    /** Serialization version number. */
    private static final long serialVersionUID = 1;

    private int newRevisions = 0;

    private ChannelDataSet<DocumentChannelData> revisions;

    private Document currentDocument;

    public ClientDocumentChannel(final ChannelName name)
            throws RemoteException {

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

        ActionEvent event = new ActionEvent(
                this, ActionEvent.ACTION_FIRST, "Message Added");
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


    public void deleteParagraph(ParagraphIdentifier id) throws RemoteException {
        currentDocument.delete(id);
    }

    public void insertText(final int offset, final String content, ParagraphIdentifier id) throws RemoteException {
        currentDocument.get(id).insert(offset, content);
        //TODO: actually queue up changes, send to server after time or reqs are met
    }

    public void deleteText(final int offset, final int length, ParagraphIdentifier id) throws RemoteException {
        currentDocument.get(id).delete(offset, length);
        //TODO: actually queue up changes, send to server after time or reqs are met
    }

    public void changeHeaderLevel(final int headerLevel, ParagraphIdentifier id) throws RemoteException {
        currentDocument.get(id).setHeaderLevel(headerLevel);
        //TODO: actually queue up changes, send to server after time or reqs are met
    }

    public void requestLock(UserName lockHolder, ParagraphIdentifier id) throws RemoteException {
        add(new LockDocChannelData(lockHolder, id));
    }
    
    public void requestUnlock(ParagraphIdentifier id) throws RemoteException {
        add(new LockDocChannelData(null, id));
    }
}
