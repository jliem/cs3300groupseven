package colab.server.channel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import colab.common.Document;
import colab.common.channel.ChannelDataStore;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.DocumentChannelData;
import colab.common.channel.DocumentDataSet;
import colab.common.channel.type.DocumentChannelType;
import colab.common.exception.NotApplicableException;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;

/**
 * ServerChatChannel is a type of {@link ServerChannel} which
 * deals with document channels.
 */
public final class ServerDocumentChannel
        extends ServerChannel<DocumentChannelData> {

    /** The channel data. */
    private ChannelDataStore<DocumentChannelData> revisions;

    private Document currentDocument;
    
    /**
     * Constructs a new server-side chat channel.
     *
     * @param name the name of the channel
     */
    public ServerDocumentChannel(final ChannelName name) {

        super(name);
        this.revisions = new DocumentDataSet();
        this.currentDocument = new Document();
    }

    //TODO: implement file stuff for document channels
    //A BIG DEAL
    /** Constructs a new server-side chat channel.
    *
    * @param name the name of the channel
    * @param file the file to use for data storage
    * @throws IOException if a file storage error occurs
    */
   public ServerDocumentChannel(final ChannelName name, final File file)
           throws IOException {

       super(name);

       throw new IllegalStateException("ServerDocumentChannel's constructor "
               + "with file parameter is not implemented");

//       ChannelFile<DocumentChannelData> channelFile =
//           new ChannelFile<DocumentChannelData>(
//               file, DocumentChannelData.getXmlConstructor());
//       this.revisions = channelFile;
   }

    /** {@inheritDoc} */
    @Override
    public void add(final DocumentChannelData data) {

        Document test = currentDocument.copy();
        //check for channel data validity
        try {
            data.apply(test);
        }
        catch(NotApplicableException ex){ 
            return;
        }
        
        try {
            data.apply(currentDocument);
        }
        catch(NotApplicableException e) { 
            //guaranteed to never happen =)
        }
        
        // Store the data, and assign it an identifier.
        data.setId(null);
        revisions.add(data);

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
