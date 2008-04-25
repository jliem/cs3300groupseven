package colab.client.gui.revision;

import java.util.Vector;

import javax.swing.JTextArea;

import colab.client.ClientChannel;
import colab.common.DebugManager;
import colab.common.channel.ChannelDataIdentifier;
import colab.common.channel.ChannelDataSet;
import colab.common.channel.document.Document;
import colab.common.channel.document.DocumentChannelData;
import colab.common.channel.document.DocumentParagraph;
import colab.common.channel.document.InsertDocChannelData;
import colab.common.channel.document.DocumentChannelData.DocumentChannelDataType;
import colab.common.channel.whiteboard.layer.LayerIdentifier;

public class RevisionDocumentPanel extends RevisionPanel {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private final ChannelDataSet<DocumentChannelData> dataSet;

    private final JTextArea text;

    public RevisionDocumentPanel(final ClientChannel channel,
            final ChannelDataSet<DocumentChannelData> dataSet) {

        super(channel);

        this.dataSet = dataSet;

        DebugManager.debug("Revision panel has " + dataSet.getAll().size() + " revisions");

        text = new JTextArea();
        text.setWrapStyleWord(true);
        text.setEditable(false);

        addToDisplay(text);
    }

    @SuppressWarnings("unchecked")
    protected Vector<Revision> buildRevisionList() {
        Vector<Revision> list = new Vector<Revision>();

        ChannelDataSet<DocumentChannelData> dataSet =
            getChannel().getChannelData();
        for (Object o : dataSet.getAll()) {
            DocumentChannelData data = (DocumentChannelData)o;

            if (data != null && data.getCreator() != null
                    && data.getTimestamp() != null) {
                // Don't add locks
                if (data.getType() != DocumentChannelDataType.LOCK) {
                    list.add(new Revision(data));
                }
            }
        }

        return list;

    }

    protected void showRevision(final ChannelDataIdentifier dataID) {

        Document doc = new Document();



        for (DocumentChannelData data : dataSet.getAll()) {

            // If it's an insert, copy the layer first and clear it
            if (data instanceof InsertDocChannelData) {
                InsertDocChannelData insert = ((InsertDocChannelData)data);

                // Create a copy of the paragraph, then clear it
                insert = insert.copy();
                //insert.getParagraph().clear();

                data = insert;

                DebugManager.debug("Got insert: " + insert.getId());
            }

            DebugManager.debug("Applying " + data);

            try {
                data.apply(doc);
            } catch (Exception e) {
                DebugManager.shouldNotHappen(e);
            }

            if (data.getId().equals(dataID)) {
                break;
            }

        }

        int numParagraphs = doc.getNumberParagraphs();

        text.setText("");
        for (int i=0; i<numParagraphs; i++) {
            DocumentParagraph paragraph = doc.get(i);
            text.setText(text.getText() + paragraph.getContents() + "\n");
        }

    }

}
