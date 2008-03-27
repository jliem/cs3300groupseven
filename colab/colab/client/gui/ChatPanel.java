package colab.client.gui;

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

import colab.common.channel.ChatChannelData;
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

    private Dimension lastSize;

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
                            // additional "write message" functionality
                            // TODO: should probably be refactored to own method
                            pendingMessages.addLast(
                                    new ChatChannelData(textbox.getText(),
                                            username));
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

        lastSize = getSize();

    }

    /**
     * Adds new chat input to original chat dialog.
     *
     * @param message the message posted by the user
     */
    public final void writeMessage(final ChatChannelData message) {

        textArea.append(message.getMessageString(timestampEnabled) + "\n");

        JScrollBar bar = chatScroll.getVerticalScrollBar();
        boolean autoScroll =
            ((bar.getValue() + bar.getVisibleAmount()) == bar.getMaximum());

        if (autoScroll) {
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }

    }

    public ChatChannelData dequeuePendingMessage() {

        if (pendingMessages.size() > 0) {
            return pendingMessages.removeFirst();
        }

        return null;

    }

    public boolean isTimestampEnabled() {

        return timestampEnabled;

    }

    public void setTimestampEnabled(final boolean timestampEnabled) {

        this.timestampEnabled = timestampEnabled;

    }

    public static void main(final String[] args) {

        JFrame f = new JFrame();
        f.setSize(320, 300);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ChatPanel p = new ChatPanel(new UserName("test"));
        f.setContentPane(p);

        f.pack();
        f.setVisible(true);

    }

}
