package colab.common;

import java.util.ArrayList;

public class DocumentParagraphDiff {
    private interface Applicable {
        public void apply(DocumentParagraph para) throws Exception;
    }
    
    private class Insert implements Applicable {
        
        private String contents;
        
        private int offset;
        
        public Insert(int offset, String contents) {
            super();
            this.contents = contents;
            this.offset = offset;
        }

        public void apply(DocumentParagraph para) throws Exception {
            if(offset >= para.getLength()) {
                throw new Exception("Insert outside of range of paragraph.");
            }
            else {
                para.insert(offset, contents);
            }
        }

        public String getContents() {
            return contents;
        }
        
        public int getOffset() {
            return offset;
        }
    }
    
    private class Delete implements Applicable {
        private int offset, length;

        public Delete(int offset, int length) {
            super();
            this.offset = offset;
            this.length = length;
        }
        
        public void apply(DocumentParagraph para) throws Exception {
            if(offset + length > para.getLength()) {
                throw new Exception("Delete not in range of paragraph.");
            }
            else {
                para.delete(offset, length);
            }
        }
        
        public int getLength() {
            return length;
        }

        public int getOffset() {
            return offset;
        }
    }
    
    private class ChangeLevel implements Applicable {
        private int headerLevel;

        public ChangeLevel(int headerLevel) {
            super();
            this.headerLevel = headerLevel;
        }

        public void apply(DocumentParagraph para) throws Exception {
            para.setHeaderLevel(headerLevel);
        }
        
        public int getHeaderLevel() {
            return headerLevel;
        }
    }
    
    private ArrayList<Applicable> changes;
    
    public DocumentParagraphDiff() {
        changes = new ArrayList<Applicable>();
    }
    
    protected DocumentParagraphDiff(ArrayList<Applicable> changes) {
        this.changes = changes;
    }
    
    public DocumentParagraph apply(DocumentParagraph paragraph) {
        DocumentParagraph ret = paragraph.copy();
        
        for(Applicable change : changes) {
            try {
                change.apply(ret);
            } catch (Exception e) {
                e.printStackTrace();
                ret = null;
                break;
            }
        }
        
       return ret;
    }
    
    public void insert(int offset, String content) {
        changes.add(new Insert(offset, content));
    }
    
    public void delete(int offset, int length) {
        changes.add(new Delete(offset, length));
    }
    
    public void changeHeaderLevel(int headerLevel) {
        changes.add(new ChangeLevel(headerLevel));
    }
    
    public void reset() {
        changes.clear();
    }
    
    public int getNumberChanges() {
        return changes.size();
    }
    
    public boolean hasChanges() {
        return changes.size()>0;
    }
    
    public DocumentParagraphDiff copy() {
        ArrayList<Applicable> list = new ArrayList<Applicable>(changes);
        return new DocumentParagraphDiff(list);
    }
}