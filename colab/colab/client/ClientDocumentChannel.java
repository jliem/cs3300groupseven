package colab.client;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.List;

import colab.common.DebugManager;
import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDataSet;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.document.Document;
import colab.common.channel.document.DocumentChannelData;
import colab.common.channel.document.EditDocChannelData;
import colab.common.channel.document.LockDocChannelData;
import colab.common.channel.type.DocumentChannelType;
import colab.common.exception.NotApplicableException;
import colab.common.identity.ParagraphIdentifier;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;

public final class ClientDocumentChannel extends ClientChannel {

    /** Serialization version number. */
    private static final long serialVersionUID = 1L;

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
            if (data instanceof EditDocChannelData) {
                EditDocChannelData edit = (EditDocChannelData)data;
            }
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


    public void deleteParagraph(final ParagraphIdentifier id)
            throws RemoteException {
        currentDocument.delete(id);
    }

    public void changeHeaderLevel(final int headerLevel,
            final ParagraphIdentifier id) throws RemoteException {
        currentDocument.get(id).setHeaderLevel(headerLevel);
    }

    public void requestLock(final UserName lockHolder,
            final ParagraphIdentifier id) throws RemoteException {
        add(new LockDocChannelData(lockHolder, id));
    }

    public void requestUnlock(final ParagraphIdentifier id)
            throws RemoteException {
        add(new LockDocChannelData(null, id));
    }

    public void export(final File file) throws IOException {

        PrintWriter writer = new PrintWriter(new FileOutputStream(file));

        try {
            writer.println("<html>");
            writer.println("<head>");
            writer.println("<title>"
                    + this.getChannelDescriptor().getName().getValue()
                    + "</title>");
            writer.println("<style type=\"text/css\">");
            writer.println("p { font-family: Serif; }");
            writer.println(".level0 { font-size: 1.00em; }");
            writer.println(".level1 { font-size: 1.25em; }");
            writer.println(".level2 { font-size: 1.50em; }");
            writer.println(".level3 { font-size: 1.75em; }");
            writer.println(".level4 { font-size: 2.00em; }");
            writer.println(".level5 { font-size: 2.25em; }");
            writer.println(".level6 { font-size: 2.50em; }");
            writer.println("</style>");
            writer.println("</head>");
            writer.println("<body>");

            this.currentDocument.export(writer);

            writer.println("</body>");
            writer.println("</html>");
        } finally {
            writer.close();
        }

    }

}
