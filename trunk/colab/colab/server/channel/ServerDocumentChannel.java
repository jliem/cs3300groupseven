package colab.server.channel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import colab.common.channel.ChannelDataStore;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.ChannelType;
import colab.common.channel.DocumentChannelData;
import colab.common.channel.DocumentDataSet;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;

public class ServerDocumentChannel extends ServerChannel<DocumentChannelData> {

    /** The channel data. */
    private ChannelDataStore<DocumentChannelData> revisions;
    
    /**
     * Constructs a new server-side chat channel.
     *
     * @param name the name of the channel
     */
    public ServerDocumentChannel(final ChannelName name) {

        super(name);
        this.revisions = new DocumentDataSet();

    }
    
    
    //TODO: implement file stuff for document channels
    //A BIG DEAL
    /** Constructs a new server-side chat channel.
    *
    * @param name the name of the channel
    * @param file the file to use for data storage
    * @throws IOException if a file storage error occurs
    */
   /*public ServerDocumentChannel(final ChannelName name, final File file)
           throws IOException {

       super(name);
       ChannelFile<DocumentChannelData> channelFile =
           new ChannelFile<DocumentChannelData>(
               file, DocumentChannelData.getXmlConstructor());
       this.revisions = channelFile;
   }*/
    
    @Override
    /** {@inheritDoc} */
    public void add(final DocumentChannelData data) {

        // Store the data, and assign it an identifier.
        data.setId(null);
        revisions.add(data);

        // Forward it to all clients, regardless of the creator
        sendToAllRegardless(data);
    }
    
    protected void sendToAllRegardless(DocumentChannelData data) {
        UserName temp = data.getCreator();
        data.setCreator(null);
        super.sendToAll(data);
        data.setCreator(temp);
    }

    @Override
    /** {@inheritDoc} */
    public ChannelDescriptor getChannelDescriptor() {
        return new ChannelDescriptor(this.getId(), ChannelType.DOCUMENT);
    }

    @Override
    /** {@inheritDoc} */
    public List<DocumentChannelData> getLastData(int count) {
        List<DocumentChannelData> list = new ArrayList<DocumentChannelData>();
        for(DocumentChannelData dcd : revisions.getLast(count)) {
            list.add(dcd);
        }
        return list;
    }
}
