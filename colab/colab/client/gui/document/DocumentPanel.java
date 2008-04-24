package colab.client.gui.document;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
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
import colab.common.channel.document.DeleteDocChannelData;
import colab.common.channel.document.Document;
import colab.common.channel.document.DocumentChannelData;
import colab.common.channel.document.DocumentParagraph;
import colab.common.channel.document.InsertDocChannelData;
import colab.common.event.document.DocumentListener;
import colab.common.identity.ParagraphIdentifier;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;

/**
 * Panel which displays the UI for a document channel.
 */
public final class DocumentPanel extends ClientChannelPanel {

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
     * @param channel the client channel backing this document
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

        //ParagraphEditor newParagraph = addParagraph(new DocumentParagraph(
        //        "", 0, username, new ParagraphIdentifier(new Integer(0)),
        //        new Date()));

        arrangePanel();

        this.document.addDocumentListener(new DocumentListener() {
            public void onInsert(final int offset,
                    final DocumentParagraph paragraph) {
                ParagraphEditor pe = insertParagraphEditor(offset, paragraph);
                arrangePanel();

                pe.requestFocus();
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
        //addTestParagraphs();

    }

    /**
     * Redownloads document from client channel.
     */
    public void refreshDocument() {
        editors = Collections.synchronizedList(
                new ArrayList<ParagraphEditor>());

        /* Potential sync issues (what if something
         * is added while the window is being built?) */
        Iterator<DocumentParagraph> iter = this.document.paragraphIterator();
        int paragraphCount = 0;
        while (iter.hasNext()) {
            DocumentParagraph para = iter.next();
            insertParagraphEditor(paragraphCount, para);
            paragraphCount++;
        }
    }

    /**
     * Creates a new paragraph.
     *
     * @param previous the id of the paragraph after which to insert
     */
    public void createNewParagraph(final ParagraphIdentifier previous) {
        InsertDocChannelData data =
            new InsertDocChannelData(previous, getUsername());
        this.fireOnMessageSent(data);
    }

    /**
     * Removes a paragraph from the document.
     *
     * @param id the id of the paragraph to remove
     */
    public void deleteParagraph(final ParagraphIdentifier id) {

        DeleteDocChannelData data = new DeleteDocChannelData(id,
                getUsername(), new Date());
        this.fireOnMessageSent(data);
    }

    /**
     * @param listener a listener to add
     */
    public void addChannelPanelListener(
            final ChannelPanelListener listener) {

        channelListeners.add(listener);

    }

    /**
     * @param listener a listener to remove
     */
    public void removeChannelPanelListener(
            final ChannelPanelListener listener) {

        channelListeners.remove(listener);

    }

    /**
     * Informs all listeners that some data needs to be sent out.
     *
     * @param dcd the document channel data generated by this panel
     */
    public void fireOnMessageSent(final DocumentChannelData dcd) {
        for (final ChannelPanelListener l : channelListeners) {
            l.onMessageSent(dcd);
        }
    }

    /**
     * @return the number of paragraphs in the document
     */
    public int getNumberOfEditors() {
        return editors.size();
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

    private ParagraphEditor insertParagraphEditor(final int offset,
            final DocumentParagraph paragraph) {

        final ParagraphEditor editor =
            new ParagraphEditor(channel, this, paragraph, getUsername());

        editor.addParagraphListener(
                new ParagraphChangeMerger(this, paragraph.getId()));

        editors.add(offset, editor);
        return editor;
    }

    /**
     * Moves focus to the next available paragraph editor.
     *
     * @param fromThisOne the editor which currently has focus
     */
    public void shiftFocus(final ParagraphEditor fromThisOne) {

        boolean found = false;

        Iterator<ParagraphEditor> iter = editors.iterator();

        while (iter.hasNext()) {
            if (iter.next() == fromThisOne) {
                break;
            }
        }

        while (iter.hasNext()) {
            ParagraphEditor next = iter.next();
            if (next.isUnlocked()) {
                next.requestFocus();
                found = true;
                break;
            }
        }

        if (!found) {
            iter = editors.iterator();
            while(iter.hasNext()) {
                ParagraphEditor next = iter.next();
                if (next.isUnlocked()) {
                    next.requestFocus();
                    break;
                }
            }
        }

    }

    /**
     * A main method for testing this panel.
     *
     * @param args unused
     * @throws Exception if any exception is thrown
     */
    public static void main(final String[] args) throws Exception {

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
