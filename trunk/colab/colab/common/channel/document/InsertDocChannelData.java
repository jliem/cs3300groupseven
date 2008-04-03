package colab.common.channel.document;

import java.util.Date;

import colab.common.exception.NotApplicableException;
import colab.common.naming.UserName;

public final class InsertDocChannelData extends DocumentChannelData {

    /** Serialization version number. */
    private static final long serialVersionUID = 1;

    private int offset;

    private DocumentParagraph paragraph;

    public InsertDocChannelData(final int offset,
            final DocumentParagraph paragraph,
            final UserName creator, final Date timestamp) {

        super(creator, timestamp, DocumentChannelDataType.INSERT);

        this.offset = offset;
        this.paragraph = paragraph;

    }

    @Override
    public void apply(final Document doc) throws NotApplicableException {
        if(offset > doc.getNumberParagraphs()) {
            throw new NotApplicableException(
                    "Offset outside of document limits.");
        }
        doc.insert(offset, paragraph);
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(final int offset) {
        this.offset = offset;
    }

    public DocumentParagraph getParagraph() {
        return paragraph;
    }

    public void setParagraph(final DocumentParagraph paragraph) {
        this.paragraph = paragraph;
    }

}
