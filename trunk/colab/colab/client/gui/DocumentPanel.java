package colab.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import colab.common.DeleteParagraphListener;
import colab.common.Document;
import colab.common.DocumentParagraph;
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
    
    private ArrayList<ParagraphEditor> editors;
    
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
        editors = new ArrayList<ParagraphEditor>();
        
        //TODO: add pagragraph editors already present
        
        document.addInsertParagraphListener(new InsertParagraphListener() {
            public void onInsert(int offset, DocumentParagraph paragraph) {
                ParagraphEditor editor = new ParagraphEditor(paragraph, name);
                
                //TODO: listeners
                
                editors.add(offset, editor);
            }
        });
        
        document.addDeleteParagraphListener(new DeleteParagraphListener() {
           public void onDelete(ParagraphIdentifier id) {
                Iterator<ParagraphEditor> iter = editors.iterator();
                
                while(iter.hasNext()) {
                    ParagraphEditor next = iter.next();
                    if(next.getParagraph().getId().equals(id)) {
                        iter.remove();
                        break;
                    }
                }
            } 
        });
        
        add(scroll, BorderLayout.CENTER);
    }

    public void apply(DocumentChannelData dcd) throws NotApplicableException {
        dcd.apply(document);
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
        
        doc.insert(0, new DocumentParagraph("Our first paragraph.", 0, new UserName("Matt"), new Date()));
    }
}

class ParagraphEditor extends JTextPane {
    
    private final DocumentParagraph paragraph;
    
    private final UserName user;
    
    private static final long serialVersionUID = 1;
    
    public ParagraphEditor(final DocumentParagraph paragraph, final UserName user) {
        this.paragraph = paragraph;
        this.user = user;
        
        
        
        paragraph.addParagraphListener(new ParagraphListener() {
           public void onHeaderChange(int headerLevel) {
            // TODO Auto-generated method stub
               
           }
           public void onDelete(int offset, int length) {
               StringBuffer buffer = new StringBuffer(getText());
               
               buffer.delete(offset, length);
               
               setText(buffer.toString());
           }
           public void onInsert(int offset, String hunk) {
               StringBuffer buffer = new StringBuffer(getText());
               
               buffer.insert(offset, hunk);
               
               setText(buffer.toString());
           }
           public void onLock(UserName newOwner) {
               if(newOwner.equals(user)) {
                
                   
               }
               else {
                   
                   setEditable(false);
               }
           }
           public void onUnlock() {
            // TODO Auto-generated method stub
               
               setEditable(true);
           }
        });
    }
    
    public DocumentParagraph getParagraph() {
        return paragraph;
    }
}