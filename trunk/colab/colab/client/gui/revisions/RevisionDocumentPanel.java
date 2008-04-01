package colab.client.gui.revisions;

import javax.swing.JTextArea;

import colab.client.ClientChannel;
import colab.common.DebugManager;
import colab.common.Document;
import colab.common.DocumentParagraph;
import colab.common.channel.ChannelDataIdentifier;
import colab.common.channel.ChannelDataSet;
import colab.common.channel.DocumentChannelData;

public class RevisionDocumentPanel extends RevisionPanel {

    private final ChannelDataSet<DocumentChannelData> dataSet;

    private final JTextArea text;

    public RevisionDocumentPanel(final ClientChannel channel,
            final ChannelDataSet<DocumentChannelData> dataSet) {

        super(channel);

        this.dataSet = dataSet;

        text = new JTextArea();
        text.setWrapStyleWord(true);
        text.setEditable(false);

        addToDisplay(text);
    }

    protected void showRevision(final ChannelDataIdentifier dataID) {

        Document doc = new Document();

        for (DocumentChannelData data : dataSet.getAll()) {

            try {
                data.apply(doc);
            } catch (Exception e) {
                DebugManager.shouldNotHappen(e);
            }

            if (data.getId().equals(dataID))
                break;
        }

        int numParagraphs = doc.getNumberParagraphs();

        text.setText("");
        for (int i=0; i<numParagraphs; i++) {
            DocumentParagraph paragraph = doc.get(i);
            text.setText(text.getText() + paragraph.getContents() + "\n");
        }

    }

}
