package colab.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import colab.common.channel.ChatChannelData;
import colab.common.naming.UserName;

class ChatPanel extends ClientChannelPanel {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private ArrayList<ActionListener> listeners;

    private final String user;

    private final JScrollPane chatScroll, sendScroll;

    private final JTextArea textArea, textbox;

    private LinkedList<ChatChannelData> pendingMessages;

    private Dimension lastSize;

    private boolean timestampEnabled = false;

    public ChatPanel(final UserName name) {

        listeners = new ArrayList<ActionListener>();
        textArea = new JTextArea(10, 30);
        textbox = new JTextArea(2, 20);

        user = new String(name.toString());
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
                                            new UserName(user)));
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
        add(userListPanel, BorderLayout.EAST);

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

    /**
     * Adds an ActionListener of a certain type to an element.
     *
     * @param listener the particular ActionListener
     */
    public final void addActionListener(final ActionListener listener) {
        listeners.add(listener);
    }

    /**
     * Fires when an ActionEvent occurs.
     *
     * @param event the ActionEvent that occurs
     */
    protected final void fireActionPerformed(final ActionEvent event) {

        for (final ActionListener listener : listeners) {
            listener.actionPerformed(event);
        }

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
