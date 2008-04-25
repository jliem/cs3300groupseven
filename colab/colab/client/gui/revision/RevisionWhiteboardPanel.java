package colab.client.gui.revision;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import colab.client.ClientChannel;
import colab.common.DebugManager;
import colab.common.channel.ChannelDataIdentifier;
import colab.common.channel.ChannelDataSet;
import colab.common.channel.whiteboard.EditLayer;
import colab.common.channel.whiteboard.InsertLayer;
import colab.common.channel.whiteboard.Whiteboard;
import colab.common.channel.whiteboard.WhiteboardChannelData;
import colab.common.channel.whiteboard.layer.LayerIdentifier;

public class RevisionWhiteboardPanel extends RevisionPanel {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private ClientChannel channel;

    private RevisionWhiteboardDisplayPanel display;
    private JList revisionList;
    private JScrollPane revisionScroll;
    private Vector<Revision> revisions;

    private final ChannelDataSet<WhiteboardChannelData> dataSet;

    public RevisionWhiteboardPanel(final ClientChannel channel,
            final ChannelDataSet<WhiteboardChannelData> dataSet) {

        this.channel = channel;
        this.dataSet = dataSet;

        display = new RevisionWhiteboardDisplayPanel();
        revisions = this.buildRevisionList();

        revisionList = new JList(revisions);
        revisionScroll = new JScrollPane(revisionList);

        revisionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        revisionList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() > 1) {
                    Revision rev = (Revision)(revisionList.getSelectedValue());
                    showRevision(rev.getId());
                }
            }
        });

        this.setLayout(new GridLayout(2, 1));
        add(revisionScroll);
        add(display);

    }

    protected ClientChannel getChannel() {
        return channel;
    }

    protected Component addToDisplay(final Component c) {
        return display.add(c);
    }

    @SuppressWarnings("unchecked")
    protected Vector<Revision> buildRevisionList() {
        Vector<Revision> list = new Vector<Revision>();

        ChannelDataSet<WhiteboardChannelData> dataSet =
            getChannel().getChannelData();
        for (Object o : dataSet.getAll()) {
            WhiteboardChannelData data = (WhiteboardChannelData)o;

            if (data != null && data.getCreator() != null
                    && data.getTimestamp() != null) {

                // Only add edits
                if (data instanceof EditLayer) {
                    list.add(new Revision(data));
                }
            }
        }

        return list;

    }

    protected void showRevision(final ChannelDataIdentifier dataID) {

        Whiteboard board = new Whiteboard();

        for (WhiteboardChannelData data : dataSet.getAll()) {

            // If it's an insert, copy the layer first and clear it
            if (data instanceof InsertLayer) {
                InsertLayer insert = ((InsertLayer)data);

                // Create a copy of the layer, then clear it
                insert = insert.copy();
                insert.getLayer().clear();

                insert.getLayer().setId(new LayerIdentifier(insert.getId()));

                data = insert;
            }

            try {
                data.apply(board);
            } catch (Exception e) {
                DebugManager.shouldNotHappen(e);
            }

            if (data.getId().equals(dataID)) {
                break;
            }
        }

        display.setBoard(board);
        repaint();


    }

}
