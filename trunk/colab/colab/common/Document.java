package colab.common;

import java.util.ArrayList;

import colab.common.identity.ParagraphIdentifier;

public class Document {
    private ArrayList<DocumentParagraph> paragraphs;
    private ArrayList<InsertParagraphListener> insertListeners;
    private ArrayList<DeleteParagraphListener> deleteListeners;
    
    public Document() {
        paragraphs = new ArrayList<DocumentParagraph>();
        insertListeners = new ArrayList<InsertParagraphListener>();
        deleteListeners = new ArrayList<DeleteParagraphListener>();
    }
    
    public void insert(int offset, DocumentParagraph paragraph) {
        if(offset <= paragraphs.size() && offset >= 0) {
            paragraphs.add(offset, paragraph);
            fireOnInsert(offset, paragraph);
        }
    }
    
    public void delete(ParagraphIdentifier id) {
        DocumentParagraph rem = null;
        
        for(DocumentParagraph par : paragraphs) {
            if(par.getId().equals(id)) {
                rem = par;
                break;
            }
        }
        
        if(rem!=null) {
            paragraphs.remove(rem);
            fireOnDelete(id);
        }
    }
    
    public void applyEdit(ParagraphIdentifier id, DocumentParagraphDiff diff) {
        for(DocumentParagraph par : paragraphs) {
            if(id.equals(par.getId())) {
                diff.apply(par);
                break;
            }
        }
    }
    
    public DocumentParagraph get(int index) {
        return paragraphs.get(index);
    }
    
    public int getNumberParagraphs() {
        return paragraphs.size();
    }
    
    public void addInsertParagraphListener(InsertParagraphListener listener) {
        insertListeners.add(listener);
    }
    
    public void addDeleteParagraphListener(DeleteParagraphListener listener) {
        deleteListeners.add(listener);
    }
    
    protected void fireOnInsert(int offset, DocumentParagraph paragraph) {
        for(InsertParagraphListener listener : insertListeners) {
            listener.onInsert(offset, paragraph);
        }
    }
    
    protected void fireOnDelete(ParagraphIdentifier id) {
        for(DeleteParagraphListener listener : deleteListeners) {
            listener.onDelete(id);
        }
    }
}

interface InsertParagraphListener {
    public void onInsert(int offset, DocumentParagraph paragraph);
}

interface DeleteParagraphListener {
    public void onDelete(ParagraphIdentifier id);
}