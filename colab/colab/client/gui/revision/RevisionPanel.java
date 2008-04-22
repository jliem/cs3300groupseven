package colab.client.gui.revision;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import colab.client.ClientChannel;
import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDataIdentifier;

public abstract class RevisionPanel extends JPanel {

    private ClientChannel channel;

    private JPanel display;
    private JList revisionList;
    private JScrollPane revisionScroll;
    private Vector<Revision> revisions;

    public RevisionPanel() {

    }

    public RevisionPanel(final ClientChannel channel) {
        this.channel = channel;

        display = new JPanel();
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

    protected abstract void showRevision(ChannelDataIdentifier dataID);

    protected abstract Vector<Revision> buildRevisionList();

    protected class Revision {
        private ChannelData data;

        public Revision(final ChannelData data) {
            this.data = data;
        }

        public ChannelDataIdentifier getId() {
            return data.getId();
        }
        public String toString() {
            return this.data.getId() + " - " + data.getCreator().toString()
                    + ", " + data.getTimestamp();
        }
    }

}
