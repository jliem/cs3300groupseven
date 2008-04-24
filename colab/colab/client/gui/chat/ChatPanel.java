package colab.client.gui.chat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import colab.client.gui.ClientChannelPanel;
import colab.common.channel.chat.ChatChannelData;
import colab.common.naming.UserName;

/**
 * Panel which displays the UI for a chat channel.
 */
final class ChatPanel extends ClientChannelPanel {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private final JScrollPane chatScroll, sendScroll;

    private final JTextArea textArea, textbox;

    private LinkedList<ChatChannelData> pendingMessages;

    private boolean timestampEnabled = false;

    /**
     * Constructs a new ChatPanel.
     *
     * @param name the name of the currently logged-in user
     */
    public ChatPanel(final UserName name) {

        super(name);

        textArea = new JTextArea(10, 30);
        textbox = new JTextArea(2, 20);

        pendingMessages = new LinkedList<ChatChannelData>();

        chatScroll = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);

        sendScroll = new JScrollPane(textbox);
        textbox.setLineWrap(true);

        textbox.addKeyListener(new KeyAdapter() {
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (e.isShiftDown()) {
                        textbox.append("\n");
                    } else {
                        if (!textbox.getText().matches("\\A\\s*\\z")) {
                            // Additional "write message" functionality
                            // Should probably be refactored to own method
                            pendingMessages.addLast(
                                    new ChatChannelData(textbox.getText(),
                                            getUsername()));
                            textbox.setText("");
                            fireActionPerformed(new ActionEvent(this,
                                    ActionEvent.ACTION_FIRST, "Message Sent"));
                        }
                    }
                    e.consume();
                }
            }
        });

        setPreferredSize(new Dimension(300, 275));
        setLayout(new BorderLayout());

        add(chatScroll, BorderLayout.CENTER);
        add(sendScroll, BorderLayout.SOUTH);

    }

    /**
     * Adds new chat input to original chat dialog.
     *
     * @param message the message posted by the user
     */
    public void writeMessage(final ChatChannelData message) {

        textArea.append(message.getMessageString(timestampEnabled) + "\n");

        JScrollBar bar = chatScroll.getVerticalScrollBar();
        boolean autoScroll =
            ((bar.getValue() + bar.getVisibleAmount()) == bar.getMaximum());

        if (autoScroll) {
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }

    }

    /**
     * @return the outgoing message to send to the server, if any
     */
    public ChatChannelData dequeuePendingMessage() {

        if (pendingMessages.size() > 0) {
            return pendingMessages.removeFirst();
        }

        return null;

    }

    /**
     * @return true if timestamps are displayed
     */
    public boolean isTimestampEnabled() {

        return timestampEnabled;

    }

    /**
     * @param timestampEnabled whether timestamps should be displayed
     */
    public void setTimestampEnabled(final boolean timestampEnabled) {

        this.timestampEnabled = timestampEnabled;

    }

    /**
     * A main method for testing this panel.
     *
     * @param args unused
     * @throws Exception if any exception is thrown
     */
    public static void main(final String[] args) throws Exception {

        JFrame f = new JFrame();
        f.setSize(320, 300);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ChatPanel p = new ChatPanel(new UserName("test"));
        f.setContentPane(p);

        f.pack();
        f.setVisible(true);

    }

}
