package colab.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import colab.common.DebugManager;
import colab.common.DeleteParagraphListener;
import colab.common.Document;
import colab.common.DocumentParagraph;
import colab.common.DocumentParagraphDiff;
import colab.common.InsertParagraphListener;
import colab.common.ParagraphListener;
import colab.common.channel.DocumentChannelData;
import colab.common.exception.NotApplicableException;
import colab.common.identity.ParagraphIdentifier;
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
    
    private ArrayList<ChannelPanelListener> listeners;
    
    private Document document;

    /**
     * Constructs a new DocumentPanel.
     *
     * @param name the name of the currently logged-in user
     */
    public DocumentPanel(final UserName name, final Document document) {
        super(name);

        mainPanel = new JPanel();
        scroll = new JScrollPane(mainPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        listeners = new ArrayList<ChannelPanelListener>();
        
        setLayout(new BorderLayout());
        
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
       
        this.document = document;
        editors = Collections.synchronizedList(new ArrayList<ParagraphEditor>());
        
        
        
        //TODO: potential sync issues (what if sometihng is added while the window is being built?)
        Iterator<DocumentParagraph> iter = this.document.paragraphIterator();
        while(iter.hasNext()) {
            addParagraph(iter.next());
        }
        arrangePanel();
        
        this.document.addInsertParagraphListener(new InsertParagraphListener() {
            public void onInsert(int offset, DocumentParagraph paragraph) {
                insertParagraphEditor(offset, paragraph);
                arrangePanel();
            }
        });
        
        this.document.addDeleteParagraphListener(new DeleteParagraphListener() {
           public void onDelete(ParagraphIdentifier id) {
                Iterator<ParagraphEditor> iter = editors.iterator();
                
                while(iter.hasNext()) {
                    ParagraphEditor next = iter.next();
                    if(next.getParagraph().getId().equals(id)) {
                        iter.remove();
                        break;
                    }
                }
                
                arrangePanel();
            } 
        });
        
        add(scroll, BorderLayout.CENTER);
    }

    public void apply(DocumentChannelData dcd) throws NotApplicableException {
        dcd.apply(document);
    }
    
    public void addChannelPanelListener(ChannelPanelListener listener) {
        listeners.add(listener);
    }
    
    public void removeChannelPanelListener(ChannelPanelListener listener) {
        listeners.remove(listener);
    }
    
    private void fireOnMessageSent(DocumentChannelData dcd) {
        for(ChannelPanelListener l : listeners) {
            l.onMessageSent(dcd);
        }
    }
    
    private void arrangePanel() {
        mainPanel.removeAll();
        for(ParagraphEditor editor : editors) {
            mainPanel.add(editor);
        }
    }
    
    private void addParagraph(DocumentParagraph para) {
        insertParagraphEditor(editors.size(), para);
    }
    
    private void insertParagraphEditor(int offset, DocumentParagraph paragraph) {
        final ParagraphEditor editor = new ParagraphEditor(paragraph, getUsername());

        editor.addKeyListener(new KeyAdapter() {
           @Override
            public void keyPressed(KeyEvent arg0) {
                super.keyPressed(arg0);
                if(arg0.getKeyCode()==KeyEvent.VK_ENTER) {
                    if(arg0.isShiftDown()) {
                        editor.append("\n");
                    }
                    else {
                        //TODO- signal new paragraph creation to server, insert in gui, move cursor to it, et cetera
                    }
                    arg0.consume();
                }
                else if(arg0.getKeyCode() == KeyEvent.VK_TAB){
                    if(arg0.isShiftDown()) {
                        editor.append("\t");
                    }
                    else {
                        shiftFocus();
                    }
                    arg0.consume();
                }
                else if(arg0.getKeyCode()== KeyEvent.VK_UP && arg0.isControlDown()) {
                    DocumentParagraph p = editor.getParagraph();
                    p.setHeaderLevel(p.getHeaderLevel()+1);
                    //TODO: - signal insert to server, still not sure how this will work- maybe some unified object that i can send all this too, will use timers to send updates?
                }
                else if(arg0.getKeyCode() == KeyEvent.VK_DOWN && arg0.isControlDown()) {
                    DocumentParagraph p = editor.getParagraph();
                    p.setHeaderLevel(p.getHeaderLevel()-1);
                    //TODO: signal server
                }
            } 
        });
        
        editors.add(offset, editor);
    }
    
    private void insertParagraphEditor(ParagraphIdentifier afterID, DocumentParagraph paragraph) {
        int i;
        
        for(i = 0; i<editors.size(); i++) {
            if(afterID.equals(editors.get(i).getParagraph().getId())) {
                break;
            }
        }
        
        insertParagraphEditor(i, paragraph);
    }
    
    private void shiftFocus() {
        //TODO: add "circularly linked list" focus traversal with editors
    }
    
    public static void main(String args[]) {
        JFrame f = new JFrame("Document Editor");
        f.setPreferredSize(new Dimension(320, 300));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Document doc = new Document();
        
        DocumentPanel p = new DocumentPanel(new UserName("Matt"), doc);
        f.setContentPane(p);
        
        f.pack();
        f.setVisible(true);
        
        doc.insert(0, new DocumentParagraph("Our first paragraph!", 4, new UserName("Matt"), new ParagraphIdentifier("Test"), new Date()));
        DocumentParagraphDiff diff = new DocumentParagraphDiff();
        diff.lock(new UserName("Matt"));
        
        
        try {
            doc.applyEdit((ParagraphIdentifier)doc.get(0).getId(), diff);
        }
        catch(NotApplicableException e) {
            DebugManager.shouldNotHappen(e);
        }
        doc.get(0).unlock();
        doc.get(0).lock(new UserName("Alex"));
        
        doc.insert(1, new DocumentParagraph("Our next paragraph.", 0, new UserName("Matt"), new ParagraphIdentifier("Test2"), new Date()));
        DocumentParagraph last = new DocumentParagraph("This paragraph currently has no lock.", 1, new UserName("Chris"), new ParagraphIdentifier("Newest"), new Date());
        doc.insert(2, last);
        last.unlock();
    }
}

class ParagraphEditor extends JTextArea {
    
    private static final int FONT_STEP = 4;
    
    private final DocumentParagraph paragraph;
    
    private final UserName user;
    
    private final Font defaultFont;
    
    private final Color defaultFG, defaultBG;
    
    private static final long serialVersionUID = 1;
    
    public ParagraphEditor(final DocumentParagraph paragraph, final UserName user) {
        this.paragraph = paragraph;
        this.user = user;
        this.defaultFont = getFont();
        this.defaultFG = getForeground();
        this.defaultBG = getBackground();
        
        setText(this.paragraph.getContents());
        showHeader(this.paragraph.getHeaderLevel());
        showLock(this.paragraph.getLockHolder());
        
        setLineWrap(true);
        setWrapStyleWord(true);
        
        paragraph.addParagraphListener(new ParagraphListener() {
           public void onHeaderChange(int headerLevel) {
               showHeader(headerLevel);
           }
           public void onDelete(int offset, int length) {
               setText(paragraph.getContents());
           }
           public void onInsert(int offset, String hunk) {
               setText(paragraph.getContents());
           }
           public void onLock(UserName newOwner) {
               showLock(newOwner);
           }
           public void onUnlock() {
               showUnlock();
           }
        });
    }
    
    public void showHeader(int headerLevel) {
        int newSize = defaultFont.getSize();
        int style = Font.PLAIN;
        
        if(headerLevel>0) {
            style = Font.BOLD;
        }
        
        if(headerLevel>1) {
            newSize += FONT_STEP * (headerLevel - 1);
        }
        
        setFont(new Font(defaultFont.getFontName(), style, newSize));
    }
    
    public void showLock(final UserName newOwner) {
        if(newOwner != null) {
            if(newOwner.equals(ParagraphEditor.this.user)) {
                setBackground(Color.BLUE);
                setForeground(Color.WHITE);
                
                setToolTipText("");
                
                requestFocus();
            }
            else {
                setBackground(Color.GREEN);
                setForeground(Color.BLACK);
                
                setToolTipText(newOwner.toString() + " is editing...");
                
                setEditable(false);
            }
        }
        else {
            showUnlock();
        }
    }
    
    public void showUnlock() {
        setForeground(defaultFG);
        setBackground(defaultBG);
        
        setEditable(true);
    }
    
    public DocumentParagraph getParagraph() {
        return paragraph;
    }
}