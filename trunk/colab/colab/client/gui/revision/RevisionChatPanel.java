package colab.client.gui.revision;

import javax.swing.JTextArea;

import colab.client.ClientChannel;
import colab.common.channel.ChannelDataIdentifier;
import colab.common.channel.ChannelDataSet;
import colab.common.channel.ChatChannelData;

public class RevisionChatPanel extends RevisionPanel {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private final ChannelDataSet<ChatChannelData> dataSet;

    private final JTextArea text;

    public RevisionChatPanel(final ClientChannel channel,
            final ChannelDataSet<ChatChannelData> dataSet) {

        super(channel);

        this.dataSet = dataSet;

        text = new JTextArea();
        text.setWrapStyleWord(true);
        text.setEditable(false);

        addToDisplay(text);

        super.repaint();

    }

    protected void showRevision(final ChannelDataIdentifier dataID) {

        text.setText("");
        for (ChatChannelData data : dataSet.getAll()) {

            text.setText(text.getText() + data.getMessageString(true) + "\n");

            if (data.getId().equals(dataID)) {
                break;
            }

        }
    }

}
