package colab.common.channel.document.diff;

import colab.common.channel.document.DocumentParagraph;
import colab.common.exception.NotApplicableException;

class Delete implements Applicable {

    private int offset;

    private int length;

    public Delete(final int offset, final int length) {
        super();
        this.offset = offset;
        this.length = length;
    }

    public void apply(final DocumentParagraph para)
            throws NotApplicableException {
        if (offset + length > para.getLength()) {
            throw new NotApplicableException(
                    "Delete not in range of paragraph.");
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
