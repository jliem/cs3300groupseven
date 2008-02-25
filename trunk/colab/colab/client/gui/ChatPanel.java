package colab.client.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import colab.common.naming.UserName;

class ChatPanel extends JPanel {

    private ArrayList<ActionListener> listeners;
    private final String user;
    private final JTextArea textArea, textbox;
    private LinkedList<String> pendingMessages;

    public ChatPanel(UserName name) {
        listeners = new ArrayList<ActionListener>();
        textArea = new JTextArea(10, 30);
        textbox = new JTextArea(2, 20);

        user = new String(name.toString());
        pendingMessages = new LinkedList<String>();

        JScrollPane textScroll = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setEditable(false);

        JScrollPane sendScroll = new JScrollPane(textbox);
        textbox.setLineWrap(true);

        textbox.addKeyListener(new KeyAdapter() {
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (e.isShiftDown()) {
                        textbox.append("\n");
                    } else {
                        if (!textbox.getText().matches("\\A\\s*\\z")) {
                            writeMessage(user + ": " + textbox.getText());
                            pendingMessages
                                    .addLast(user + ": " + textbox.getText());
                            textbox.setText("");
                            fireActionPerformed(new ActionEvent(this,
                                    ActionEvent.ACTION_FIRST, "Message Sent"));
                        }
                    }
                    e.consume();
                }
            }
        });

        textScroll.setPreferredSize(new Dimension(300, 200));
        sendScroll.setPreferredSize(new Dimension(300, 25));

        setPreferredSize(new Dimension(300, 275));
        setLayout(new FlowLayout());


        add(textScroll);
        add(sendScroll);

    }

    /**
     * Adds new chat input to original chat dialog.
     * @param mess
     *            from the user
     */
    public final void writeMessage(final String mess) {
        textArea.append(mess + "\n");
    }

    /**
     * Adds an ActionListener of a certain type to an element.
     * @param l
     *            the particular ActionListener
     */
    public final void addActionListener(final ActionListener l) {
        listeners.add(l);
    }

    /**
     * Fires when ActionEvent e occurs.
     * @param e
     *            the ActionEvent that occurs
     */
    protected final void fireActionPerformed(final ActionEvent e) {
        for (ActionListener l : listeners) {
            l.actionPerformed(e);
        }
    }

    public String dequeuePendingMessage()
    {
        if(pendingMessages.size()>0){
            return pendingMessages.removeFirst();
        }
        return null;
    }

    /**
     * Displays a ChatPanel.
     * @param args
     *            standard
     */
    public static void main(final String[] args) {
        ChatPanel p = new ChatPanel(new UserName("test!"));
        JFrame f = new JFrame();
        f.add(p);
        f.setSize(350, 355);
        f.setResizable(false);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}