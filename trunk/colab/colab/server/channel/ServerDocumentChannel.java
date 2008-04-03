package colab.server.channel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import colab.common.channel.ChannelDataSet;
import colab.common.channel.ChannelDataStore;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.document.Document;
import colab.common.channel.document.DocumentChannelData;
import colab.common.channel.document.DocumentParagraph;
import colab.common.channel.document.InsertDocChannelData;
import colab.common.channel.type.DocumentChannelType;
import colab.common.exception.NotApplicableException;
import colab.common.identity.ParagraphIdentifier;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;
import colab.server.file.ChannelFile;

/**
 * ServerChatChannel is a type of {@link ServerChannel} which
 * deals with document channels.
 */
public final class ServerDocumentChannel
        extends ServerChannel<DocumentChannelData> {

    /** The channel data. */
    private final ChannelDataStore<DocumentChannelData> revisions;

    private final Document currentDocument;

    /**
     * Constructs a new server-side document channel.
     *
     * @param name the name of the channel
     */
    public ServerDocumentChannel(final ChannelName name) {

        super(name);

        this.revisions = new ChannelDataSet<DocumentChannelData>();

        this.currentDocument = new Document();

    }

    /**
     * Constructs a new server-side document channel.
     *
     * @param name the name of the channel
     * @param file the file to use for data storage
     * @throws IOException if a file storage error occurs
     */
    public ServerDocumentChannel(final ChannelName name, final File file)
            throws IOException {

        super(name);

        ChannelFile<DocumentChannelData> channelFile =
            new ChannelFile<DocumentChannelData>(
                file, DocumentChannelData.getXmlConstructor());
        this.revisions = channelFile;

        this.currentDocument = new Document();

    }

    /** {@inheritDoc} */
    @Override
    public void add(final DocumentChannelData data) {

        try {

            // Check for channel data validity
            data.apply(currentDocument.copy());

            data.apply(currentDocument);

        } catch (final NotApplicableException ex) {
            return;
        }

        // If this is an insert, set the paragraph id
        if (data instanceof InsertDocChannelData) {
            ParagraphIdentifier paragraphId =
                new ParagraphIdentifier(data.getId());
            DocumentParagraph paragraph =
                ((InsertDocChannelData) data).getParagraph();
            paragraph.setId(paragraphId);
        }

        // Store the data, and assign it an identifier
        revisions.addAndAssignId(data);

        // Forward it to all clients, regardless of the creator
        sendToAllRegardless(data);

    }

    /**
     * Send data to all clients, including the creator.
     *
     * @param data the data to send
     */
    private void sendToAllRegardless(final DocumentChannelData data) {
        UserName temp = data.getCreator();
        data.setCreator(null);
        super.sendToAll(data);
        data.setCreator(temp);
    }

    /** {@inheritDoc} */
    @Override
    public ChannelDescriptor getChannelDescriptor() {
        return new ChannelDescriptor(this.getId(), new DocumentChannelType());
    }

    /** {@inheritDoc} */
    @Override
    public List<DocumentChannelData> getLastData(final int count) {
        List<DocumentChannelData> list = new ArrayList<DocumentChannelData>();
        for(DocumentChannelData dcd : revisions.getLast(count)) {
            list.add(dcd);
        }
        return list;
    }

}
