package colab.client.gui.revision;

import java.util.Vector;

import javax.swing.JTextArea;

import colab.client.ClientChannel;
import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDataIdentifier;
import colab.common.channel.ChannelDataSet;
import colab.common.channel.chat.ChatChannelData;

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

    protected Vector<Revision> buildRevisionList() {
        Vector<Revision> list = new Vector<Revision>();

        ChannelDataSet dataSet = getChannel().getChannelData();
        for (Object o : dataSet.getAll()) {
            ChannelData data = (ChannelData)o;

            if (data != null && data.getCreator() != null
                    && data.getTimestamp() != null) {
                list.add(new Revision(data));
            }
        }

        return list;

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
