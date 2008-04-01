package colab.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
    
    private Document document;

    /**
     * Constructs a new DocumentPanel.
     *
     * @param name the name of the currently logged-in user
     */
    public DocumentPanel(final UserName name, final Document document) {
        super(name);

        mainPanel = new JPanel();
        scroll = new JScrollPane(mainPanel);
        
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
                insertParagraph(offset, paragraph);
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
    
    private void arrangePanel() {
        mainPanel.removeAll();
        for(ParagraphEditor editor : editors) {
            mainPanel.add(editor);
        }
    }
    
    private void addParagraph(DocumentParagraph para) {
        insertParagraph(editors.size(), para);
    }
    
    private void insertParagraph(int offset, DocumentParagraph paragraph) {
        ParagraphEditor editor = new ParagraphEditor(paragraph, getUsername());
        
        //TODO: listeners (keyboard, et cetera)
        
        editors.add(offset, editor);
    }
    
    public static void main(String args[]) {
        JFrame f = new JFrame();
        f.setPreferredSize(new Dimension(320, 300));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Document doc = new Document();
        
        DocumentPanel p = new DocumentPanel(new UserName("Matt"), doc);
        f.setContentPane(p);
        
        f.pack();
        f.setVisible(true);
        
        doc.insert(0, new DocumentParagraph("Our first paragraph.", 0, new UserName("Matt"), new ParagraphIdentifier("Test"), new Date()));
        DocumentParagraphDiff diff = new DocumentParagraphDiff();
        diff.lock(new UserName("Matt"));
        
        
        try {
            doc.applyEdit((ParagraphIdentifier)doc.get(0).getId(), diff);
        }
        catch(NotApplicableException e) {
            DebugManager.shouldNotHappen(e);
            System.out.println("Woah!");
        }
        doc.get(0).unlock();
        doc.get(0).lock(new UserName("Alex"));
        
        doc.insert(1, new DocumentParagraph("Our next paragraph.", 0, new UserName("Matt"), new ParagraphIdentifier("Test2"), new Date()));
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
        setHeader(this.paragraph.getHeaderLevel());
        setLock(this.paragraph.getLockHolder());
        
        paragraph.addParagraphListener(new ParagraphListener() {
           public void onHeaderChange(int headerLevel) {
               setHeader(headerLevel);
           }
           public void onDelete(int offset, int length) {
               setText(paragraph.getContents());
           }
           public void onInsert(int offset, String hunk) {
               setText(paragraph.getContents());
           }
           public void onLock(UserName newOwner) {
               setLock(newOwner);
           }
           public void onUnlock() {
               setForeground(defaultFG);
               setBackground(defaultBG);
               
               setEditable(true);
           }
        });
    }
    
    private void setHeader(int headerLevel) {
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
    
    private void setLock(final UserName newOwner) {
        if(newOwner.equals(ParagraphEditor.this.user)) {
            setBackground(Color.BLUE);
            setForeground(Color.WHITE);
            
        }
        else {
            setBackground(Color.GREEN);
            setForeground(Color.BLACK);
            
            setEditable(false);
        }
    }
    
    public DocumentParagraph getParagraph() {
        return paragraph;
    }
}