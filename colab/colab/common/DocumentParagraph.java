package colab.common;

import java.io.Serializable;

import colab.common.naming.UserName;

public class DocumentParagraph implements Serializable {
    public static final long serialVersionUID = 1;
    
    private int headerLevel;
    
    private StringBuffer contents;
    
    private UserName lockHolder;
    
    private DocumentParagraphDiff differences;
    
    public DocumentParagraph() {
        this("", 0, null);
    }
    
    public DocumentParagraph(final String cont, int head, UserName creator) {
        headerLevel = head;
        contents = new StringBuffer(cont);
        lockHolder = creator;
        differences = new DocumentParagraphDiff();
    }
    
    protected DocumentParagraph(String cont, int head, UserName creator, DocumentParagraphDiff diff) {
        this(cont, head, creator);
        differences = diff;
    }

    public void insert(int offset, String hunk) {
        contents.insert(offset, hunk);
    }
    
    public void delete(int offset, int length) {
        contents.delete(offset, length);
    }
    
    public int getLength() {
        return contents.length();
    }
    
    public int getHeaderLevel() {
        return headerLevel;
    }

    public void setHeaderLevel(int headerLevel) {
        if(headerLevel > 0) {
            this.headerLevel = headerLevel;
        }
        else {
            this.headerLevel = 0;
        }
    }
    
    public void unlock() {
        lockHolder = null;
    }
    
    public boolean isLocked() {
        return (lockHolder != null);
    }
    
    /**
     * @param holder - the UserName that will hold the lock of this paragraph
     * @return a boolean for success- returns false if lock already held
     */
    public boolean lock(final UserName holder) {
        boolean ret = false;
        
        if(lockHolder == null) {
            lockHolder = holder;
            ret = true;
        }
        
        return ret;
    }
    
    public UserName getLockHolder() {
        return lockHolder;
    }

    public String getContents() {
        return contents.toString();
    }

    public DocumentParagraph copy() {
        //TODO: username copy method, otherwise copy could change lock in this instantiation
        return new DocumentParagraph(this.contents.toString(), this.headerLevel, this.lockHolder, this.differences.copy());
    }
    
    public DocumentParagraphDiff getDifferences() {
        DocumentParagraphDiff ret = differences.copy();
        
        resetDifferences();
        
        return ret;
    }
    
    public DocumentParagraphDiff peekDifferences() {
        return differences.copy();
    }
    
    public void resetDifferences() {
        differences.reset();
    }
    
    public boolean hasChanged() {
        return differences.hasChanges();
    }
}