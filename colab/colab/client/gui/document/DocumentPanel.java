package colab.client.gui.document;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import colab.client.ClientDocumentChannel;
import colab.client.ColabClient;
import colab.client.gui.ChannelPanelListener;
import colab.client.gui.ClientChannelPanel;
import colab.common.channel.document.Document;
import colab.common.channel.document.DocumentChannelData;
import colab.common.channel.document.DocumentParagraph;
import colab.common.event.document.DocumentListener;
import colab.common.exception.NotApplicableException;
import colab.common.identity.ParagraphIdentifier;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;

/**
 * Panel which displays the UI for a document channel.
 */
final class DocumentPanel extends ClientChannelPanel {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private JPanel mainPanel;

    private JScrollPane scroll;

    private List<ParagraphEditor> editors;

    private List<ChannelPanelListener> channelListeners;

    private Document document;

    private ClientDocumentChannel channel;

    /**
     * Constructs a new DocumentPanel.
     *
     * @param username the name of the currently logged-in user
     */
    public DocumentPanel(final UserName username,
            final ClientDocumentChannel channel) {

        super(username);

        mainPanel = new JPanel();
        scroll = new JScrollPane(mainPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        channelListeners = new ArrayList<ChannelPanelListener>();

        setLayout(new BorderLayout());

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        this.channel = channel;
        this.document = channel.getCurrentDocument();

        editors = Collections.synchronizedList(
                new ArrayList<ParagraphEditor>());

        /* TODO: potential sync issues (what if something
         * is added while the window is being built?) */
        Iterator<DocumentParagraph> iter = this.document.paragraphIterator();
        while(iter.hasNext()) {
            addParagraph(iter.next());
        }
        arrangePanel();

        this.document.addDocumentListener(new DocumentListener() {
            public void onInsert(final int offset,
                    final DocumentParagraph paragraph) {
                insertParagraphEditor(offset, paragraph);
                arrangePanel();
            }

            public void onDelete(final ParagraphIdentifier id) {

                Iterator<ParagraphEditor> iter = editors.iterator();

                while(iter.hasNext()) {
                    ParagraphEditor next = iter.next();
                    if (next.getParagraph().getId().equals(id)) {
                        iter.remove();
                        break;
                    }
                }

                arrangePanel();
            }
        });

        add(scroll, BorderLayout.CENTER);

        //***** TEST METHODS //
        addTestParagraphs();

    }

    private void addTestParagraphs() {
        document.insert(0, new DocumentParagraph(
                "Our first paragraph!", 4, super.getUsername(),
                new ParagraphIdentifier(1), new Date()));

        document.get(0).unlock();
        document.get(0).lock(new UserName("Alex"));

        document.insert(1, new DocumentParagraph(
                "Our next paragraph.", 0, new UserName("Chris"),
                new ParagraphIdentifier(2), new Date()));
        DocumentParagraph last = new DocumentParagraph(
                "This paragraph currently has no lock.", 1,
                new UserName("Chris"), new ParagraphIdentifier(5),
                new Date());
        document.insert(2, last);
        last.unlock();
    }

    public void apply(final DocumentChannelData dcd)
            throws NotApplicableException {

        dcd.apply(document);

    }

    public void addChannelPanelListener(
            final ChannelPanelListener listener) {

        channelListeners.add(listener);

    }

    public void removeChannelPanelListener(
            final ChannelPanelListener listener) {

        channelListeners.remove(listener);

    }

    private void fireOnMessageSent(final DocumentChannelData dcd) {
        for (final ChannelPanelListener l : channelListeners) {
            l.onMessageSent(dcd);
        }
    }



    private void arrangePanel() {


        mainPanel.removeAll();
        for (final ParagraphEditor editor : editors) {
            mainPanel.add(editor);
        }

        mainPanel.add(Box.createVerticalGlue());

        fireActionPerformed(new ActionEvent(this,
                ActionEvent.ACTION_FIRST, "panels arranged"));

    }

    private void addParagraph(final DocumentParagraph para) {
        insertParagraphEditor(editors.size(), para);
    }

    private void insertParagraphEditor(final int offset,
            final DocumentParagraph paragraph) {

        final ParagraphEditor editor =
            new ParagraphEditor(channel, paragraph, getUsername());

        editor.addKeyListener(new ParagraphEditorKeyAdapter(editor));
        editor.addMouseListener(new ParagraphEditorMouseAdapter(editor));

        // Add shift event
        editor.addKeyListener(new KeyAdapter() {
           @Override
            public void keyPressed(final KeyEvent arg0) {
                super.keyPressed(arg0);
                if (arg0.getKeyCode() == KeyEvent.VK_TAB) {
                    if (!arg0.isShiftDown()) {
                        shiftFocus(editor);
                    }
                    arg0.consume();
                }
           }
        });

        editors.add(offset, editor);
    }

    private void insertParagraphEditor(final ParagraphIdentifier afterID,
            final DocumentParagraph paragraph) {

        int i;

        for(i = 0; i<editors.size(); i++) {
            if(afterID.equals(editors.get(i).getParagraph().getId())) {
                break;
            }
        }

        insertParagraphEditor(i, paragraph);

    }

    private void shiftFocus(ParagraphEditor fromThisOne) {

        boolean found = false;

        Iterator<ParagraphEditor> iter = editors.iterator();

        while(iter.hasNext()) {
            if(iter.next() == fromThisOne) {
                break;
            }
        }

        while(iter.hasNext()) {
            ParagraphEditor next = iter.next();
            if(next.isUnlocked()) {
                next.requestFocus();
                found = true;
                break;
            }
        }

        if(!found) {
            iter = editors.iterator();
            while(iter.hasNext()) {
                ParagraphEditor next = iter.next();
                if(next.isUnlocked()) {
                    next.requestFocus();
                    break;
                }
            }
        }
    }

    public static void main(final String[] args) throws RemoteException {

        ClientDocumentChannel channel =
            new ClientDocumentChannel(new ChannelName("Doc test"));

        ColabClient client = new ColabClient();

        DocumentChannelFrame f = new DocumentChannelFrame(client,
                channel, new UserName("Chris"));

        f.setPreferredSize(new Dimension(320, 300));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.pack();
        f.setVisible(true);
    }

}
