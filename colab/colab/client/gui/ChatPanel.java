package colab.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import colab.common.naming.UserName;

class ChatPanel extends JPanel {

	public static final long serialVersionUID = 1;
	
    private ArrayList<ActionListener> listeners;
    private final String user;
    private final JScrollPane chatScroll, sendScroll;
    private final JTextArea textArea, textbox;
    private LinkedList<String> pendingMessages;

    private Dimension lastSize;
    
    public ChatPanel(UserName name) {
        listeners = new ArrayList<ActionListener>();
        textArea = new JTextArea(10, 30);
        textbox = new JTextArea(2, 20);

        user = new String(name.toString());
        pendingMessages = new LinkedList<String>();

        chatScroll = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setEditable(false);

        sendScroll = new JScrollPane(textbox);
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
        
        addComponentListener(new ComponentAdapter(){
        	public void componentResized(ComponentEvent e){
        		Dimension size = getSize();
            	
        		Dimension chatSize = new Dimension((int)(size.getWidth()-10), (int)(size.getHeight()-40)),
        			sendSize = new Dimension((int)(size.getWidth()-10), 25);
        		
        		chatScroll.setPreferredSize(chatSize);
        		sendScroll.setPreferredSize(sendSize);
        	}
        });

        chatScroll.setPreferredSize(new Dimension(300, 235));
        sendScroll.setPreferredSize(new Dimension(300, 25));
        
        setPreferredSize(new Dimension(300, 275));
        setLayout(new FlowLayout());


        add(chatScroll);
        add(sendScroll);

        
        lastSize = getSize();
    }

    /**
     * Adds new chat input to original chat dialog.
     * @param mess
     *            from the user
     */
    public final void writeMessage(final String mess) {
        textArea.append(mess + "\n");
        
        JScrollBar bar = chatScroll.getVerticalScrollBar();
        boolean autoScroll = ((bar.getValue() + bar.getVisibleAmount()) == bar.getMaximum());

        if( autoScroll ) {
        	textArea.setCaretPosition( textArea.getDocument().getLength() );
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
        ChatPanel p = new ChatPanel(new UserName("test"));
        JFrame f = new JFrame();
        f.setContentPane(p);
        f.setSize(320, 300);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}