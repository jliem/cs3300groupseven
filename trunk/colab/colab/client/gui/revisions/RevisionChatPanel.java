package colab.client.gui.revisions;

import javax.swing.JTextArea;

import colab.client.ClientChannel;
import colab.common.channel.ChannelDataIdentifier;
import colab.common.channel.ChatChannelData;
import colab.common.channel.ChatDataSet;

public class RevisionChatPanel extends RevisionPanel {

    private final ChatDataSet dataSet;

    private final JTextArea text;

    public RevisionChatPanel(ClientChannel channel,
            final ChatDataSet dataSet) {
        super(channel);

        this.dataSet = dataSet;

        text = new JTextArea();
        text.setWrapStyleWord(true);
        text.setEditable(false);

        addToDisplay(text);

        super.repaint();
    }

    protected void showRevision(ChannelDataIdentifier dataID) {

        text.setText("");
        for (ChatChannelData data : dataSet.getAll()) {

            text.setText(text.getText() + data.getMessageString(true) + "\n");

            if (data.getId().equals(dataID))
                break;
        }
    }

}
