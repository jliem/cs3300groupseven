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
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

class ChatPanel extends JPanel {

    private ArrayList<ActionListener> listeners;
    private final String user;
    private final JButton logoutButton, switchButton;
    private final JTextArea textArea, textbox;
    private LinkedList<String> pendingMessages;

    public ChatPanel(String username) {
        listeners = new ArrayList<ActionListener>();
        logoutButton = new JButton("Logout");
        switchButton = new JButton("Switch Communities");
        textArea = new JTextArea(10, 30);
        textbox = new JTextArea(2, 20);

        user = new String(username);
        pendingMessages = new LinkedList<String>();

        JScrollPane textScroll = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setEditable(false);

        JScrollPane sendScroll = new JScrollPane(textbox);
        textbox.setLineWrap(true);

        textbox.addKeyListener(new KeyAdapter() {
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (e.isShiftDown())
                        textbox.append("\n");
                    else {
                        writeMessage(user + ": " + textbox.getText());
                        pendingMessages
                                .addLast(user + ": " + textbox.getText());
                        textbox.setText("");
                        fireActionPerformed(new ActionEvent(this,
                                ActionEvent.ACTION_FIRST, "Message Sent"));
                    }
                }
            }
        });

        textScroll.setPreferredSize(new Dimension(300, 200));
        sendScroll.setPreferredSize(new Dimension(300, 25));

        setPreferredSize(new Dimension(300, 275));
        setLayout(new FlowLayout());

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e){
                fireActionPerformed(new ActionEvent(this,
                        ActionEvent.ACTION_FIRST, "Logout!"));
            }
        });

        switchButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e){
                fireActionPerformed(new ActionEvent(this,
                        ActionEvent.ACTION_FIRST, "Switch!"));
            }
        });

        add(textScroll);
        add(sendScroll);
        add(logoutButton);
        add(switchButton);
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

    /**
     * Displays a ChatPanel.
     * @param args
     *            standard
     */
    public static void main(final String[] args) {
        ChatPanel p = new ChatPanel("test!");
        JFrame f = new JFrame();
        f.add(p);
        f.setSize(350, 355);
        f.setResizable(false);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}