package colab.common;

import java.util.ArrayList;
import java.util.List;

import colab.common.exception.NotApplicableException;
import colab.common.naming.UserName;

/**
 * A log for paragaph inserts, deletes, and header changes.
 *
 * @see DocumentParagraph
 */
public final class DocumentParagraphDiff {

    private interface Applicable {
        void apply(DocumentParagraph para) throws NotApplicableException;
    }

    private class Insert implements Applicable {

        private String contents;

        private int offset;

        public Insert(final int offset, final String contents) {
            super();
            this.contents = contents;
            this.offset = offset;
        }

        public void apply(final DocumentParagraph para) throws NotApplicableException {
            if (offset >= para.getLength()) {
                throw new NotApplicableException("Insert outside of range of paragraph.");
            } else {
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

        private int offset;

        private int length;

        public Delete(final int offset, final int length) {
            super();
            this.offset = offset;
            this.length = length;
        }

        public void apply(final DocumentParagraph para) throws NotApplicableException {
            if (offset + length > para.getLength()) {
                throw new NotApplicableException("Delete not in range of paragraph.");
            } else {
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

        public ChangeLevel(final int headerLevel) {
            super();
            this.headerLevel = headerLevel;
        }

        public void apply(final DocumentParagraph para) throws NotApplicableException {
            if(headerLevel<0) {
                para.setHeaderLevel(headerLevel);
            }
        }

        public int getHeaderLevel() {
            return headerLevel;
        }

    }

    private class ChangeLock implements Applicable {

        private UserName newOwner;

        public ChangeLock(final UserName newOwner) {
            super();
            this.newOwner = newOwner;
        }

        public void apply(final DocumentParagraph para) {
            if(newOwner==null) {
                para.unlock();
            }
            else {
                para.lock(newOwner);
            }
        }

        public UserName getNewOwner() {
            return newOwner;
        }
    }

    private List<Applicable> changes;

    public DocumentParagraphDiff() {
        changes = new ArrayList<Applicable>();
    }

    protected DocumentParagraphDiff(final List<Applicable> changes) {
        this.changes = changes;
    }

    public DocumentParagraph apply(final DocumentParagraph paragraph) throws NotApplicableException {

        DocumentParagraph ret = paragraph.copy();

        for (Applicable change : changes) {
            try {
                change.apply(ret);
            } catch (Exception e) {
                throw new NotApplicableException(e);
            }
        }

       return ret;

    }

    public void insert(final int offset, final String content) {
        changes.add(new Insert(offset, content));
    }

    public void delete(final int offset, final int length) {
        changes.add(new Delete(offset, length));
    }

    public void changeHeaderLevel(final int headerLevel) {
        changes.add(new ChangeLevel(headerLevel));
    }

    /**
     * @param newOwner the new owner of the lock,
     *                 specifed as {@code null} to unlock
     */
    public void lock(final UserName newOwner) {
        changes.add(new ChangeLock(newOwner));
    }
    
    public void unlock() {
        changes.add(new ChangeLock(null));
    }

    public void reset() {
        changes.clear();
    }

    public int getNumberChanges() {
        return changes.size();
    }

    public boolean hasChanges() {
        return changes.size() > 0;
    }

    public DocumentParagraphDiff copy() {
        List<Applicable> list = new ArrayList<Applicable>(changes);
        return new DocumentParagraphDiff(list);
    }

}
