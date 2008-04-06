package colab.common.channel.document.diff;

import colab.common.channel.document.DocumentParagraph;
import colab.common.exception.NotApplicableException;

class Insert implements Applicable {

    private String contents;

    private int offset;

    public Insert(final int offset, final String contents) {
        super();
        this.contents = contents;
        this.offset = offset;
    }

    public void apply(final DocumentParagraph para)
            throws NotApplicableException {
        if (offset >= para.getLength()) {
            throw new NotApplicableException(
                    "Insert outside of range of paragraph.");
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
