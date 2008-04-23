package colab.server.channel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import colab.common.DebugManager;
import colab.common.channel.ChannelDataSet;
import colab.common.channel.ChannelDataStore;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.document.Document;
import colab.common.channel.document.DocumentChannelData;
import colab.common.channel.document.DocumentParagraph;
import colab.common.channel.document.InsertDocChannelData;
import colab.common.channel.document.LockDocChannelData;
import colab.common.channel.type.DocumentChannelType;
import colab.common.exception.NotApplicableException;
import colab.common.identity.ParagraphIdentifier;
import colab.common.naming.ChannelName;
import colab.server.file.ChannelFile;

/**
 * ServerDocumentChannel is a type of {@link ServerChannel} which
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
        for (DocumentChannelData data : revisions.getAll()) {
            try {
                data.apply(currentDocument);
            } catch (final NotApplicableException e) {
                DebugManager.exception(e);
            }
        }

    }

    /** {@inheritDoc} */
    @Override
    public void add(final DocumentChannelData data) {

        try {

            // Check for channel data validity
            // SHOULD take care of bad locks, et cetera
            data.apply(currentDocument.copy());

        } catch (final NotApplicableException e) {
            DebugManager.exception(e);
            return;
        }

        // If this is an insert, set the paragraph id
        if (data instanceof InsertDocChannelData) {

            InsertDocChannelData insertData = ((InsertDocChannelData) data);

            revisions.addAndAssignId(data);

            // If we don't have a paragraph already, create it
            if (insertData.getParagraph() == null) {
                ParagraphIdentifier paragraphId =
                    new ParagraphIdentifier(data.getId());

                // Create a blank paragraph with no lock holder
                DocumentParagraph paragraph =
                    new DocumentParagraph("", 0, null, paragraphId,
                            new Date());
                paragraph.setId(paragraphId);

                insertData.setParagraph(paragraph);
            }
        }

        try {
            data.apply(currentDocument);
        } catch (final NotApplicableException e) {
            DebugManager.shouldNotHappen(e);
        }

        // Store the data, and assign it an identifier
        // Might have already added this if it's an
        // InsertDocChannelData but that's ok
        if (!(data instanceof LockDocChannelData)
                && !(data instanceof InsertDocChannelData)) {
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
