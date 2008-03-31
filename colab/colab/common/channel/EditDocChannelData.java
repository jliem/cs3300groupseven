package colab.common.channel;

import java.util.Date;

import colab.common.Document;
import colab.common.DocumentParagraph;
import colab.common.DocumentParagraphDiff;
import colab.common.exception.NotApplicableException;
import colab.common.identity.ParagraphIdentifier;
import colab.common.naming.UserName;

public final class EditDocChannelData extends DocumentChannelData {

    /** Serialization version number. */
    private static final long serialVersionUID = 1L;

    private DocumentParagraphDiff differences;

    private ParagraphIdentifier paragraphID;

    public EditDocChannelData(final ParagraphIdentifier paragraphID,
            final DocumentParagraphDiff differences,
            final UserName creator, final Date timestamp) {
        super(creator, timestamp, DocumentChannelDataType.EDIT);

        this.differences = differences;
        this.paragraphID = paragraphID;
    }

    @Override
    public void apply(final Document doc) throws NotApplicableException {
        doc.applyEdit(paragraphID, differences);
    }

    public void apply(final DocumentParagraph para) throws NotApplicableException {
        if(!getCreator().equals(para.getLockHolder())) {
            throw new NotApplicableException("Editor does not hold lock.");
        }
        differences.apply(para);
    }

    public DocumentParagraphDiff getDifferences() {
        return differences;
    }

    public void setDifferences(final DocumentParagraphDiff differences) {
        this.differences = differences;
    }

    public ParagraphIdentifier getParagraphID() {
        return paragraphID;
    }

    public void setParagraphID(final ParagraphIdentifier paragraphID) {
        this.paragraphID = paragraphID;
    }

}
