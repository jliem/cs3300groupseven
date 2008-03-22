package colab.common.channel;

import java.util.Date;

import colab.common.DocumentParagraph;
import colab.common.naming.UserName;

public class InsertDocChannelData extends DocumentChannelData {

    private static final long serialVersionUID = 1;

    private int offset;

    private DocumentParagraph paragraph;

    public InsertDocChannelData(int offset, DocumentParagraph paragraph, UserName creator, Date timestamp) {
        super(creator, timestamp, DocumentChannelDataType.INSERT);

        this.offset = offset;
        this.paragraph = paragraph;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public DocumentParagraph getParagraph() {
        return paragraph;
    }

    public void setParagraph(DocumentParagraph paragraph) {
        this.paragraph = paragraph;
    }

}
