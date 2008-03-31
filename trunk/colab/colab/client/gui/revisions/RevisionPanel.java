package colab.client.gui.revisions;

import java.awt.BorderLayout;
import java.awt.Component;
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
import colab.common.channel.ChannelDataSet;

public abstract class RevisionPanel extends JPanel {

    protected ClientChannel channel;
    protected JPanel display;

    private JList revisionList;
    private JScrollPane revisionScroll;
    private Vector<Revision> revisions;

    public RevisionPanel(ClientChannel channel) {
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

        this.setLayout(new BorderLayout());
        add(revisionScroll, BorderLayout.NORTH);
        add(display, BorderLayout.CENTER);
    }

    protected Component addToDisplay(Component c) {
        return display.add(c);
    }

    protected abstract void showRevision(ChannelDataIdentifier dataID);

    private Vector<Revision> buildRevisionList() {
        Vector<Revision> list = new Vector<Revision>();

        ChannelDataSet dataSet = channel.getChannelData();
        for (Object o : dataSet.getAll()) {
            ChannelData data = (ChannelData)o;

            list.add(new Revision(data));
        }

        return list;

    }

    private class Revision {
        private ChannelData data;

        public Revision(ChannelData data) {
            this.data = data;
        }

        public ChannelDataIdentifier getId() {
            return data.getId();
        }
        public String toString() {
            return this.data.getId() + " - " + data.getCreator().toString() +
                ", " + data.getTimestamp();
        }
    }

}
