package colab.common.channel.document.diff;

import colab.common.channel.document.DocumentParagraph;
import colab.common.exception.NotApplicableException;

class ChangeLevel implements Applicable {

    private int headerLevel;

    public ChangeLevel(final int headerLevel) {
        super();
        this.headerLevel = headerLevel;
    }

    public void apply(final DocumentParagraph para)
            throws NotApplicableException {
        if (headerLevel<0) {
            para.setHeaderLevel(headerLevel);
        }
    }

    public int getHeaderLevel() {
        return headerLevel;
    }

}
