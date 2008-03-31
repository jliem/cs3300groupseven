package colab.client.gui;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BoxLayout;
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
    public DocumentPanel(final UserName name) {
        super(name);

        mainPanel = new JPanel();
        scroll = new JScrollPane(mainPanel);
        
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
       
        document = new Document();
        editors = new ArrayList<ParagraphEditor>();
        
        document.addInsertParagraphListener(new InsertParagraphListener() {
            public void onInsert(int offset, DocumentParagraph paragraph) {
                editors.add(offset, new ParagraphEditor(paragraph, name));
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
        
        add(scroll);
    }

    public void apply(DocumentChannelData dcd) throws NotApplicableException {
        dcd.apply(document);
    }
}

class ParagraphEditor extends JTextPane {
    
    private final DocumentParagraph paragraph;
    
    private final UserName user;
    
    public ParagraphEditor(final DocumentParagraph paragraph, final UserName user) {
        this.paragraph = paragraph;
        this.user = user;
        
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent arg0) {
                super.focusGained(arg0);
                
            }
        });
        
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
            // TODO Auto-generated method stub
            
           }
           public void onUnlock() {
            // TODO Auto-generated method stub
            
           }
        });
    }
    
    public DocumentParagraph getParagraph() {
        return paragraph;
    }
}