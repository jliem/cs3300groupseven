package colab.common.channel;

import java.util.Date;

import colab.common.DocumentParagraphDiff;
import colab.common.identity.ParagraphIdentifier;
import colab.common.naming.UserName;

public class EditDocChannelData extends DocumentChannelData {

    private final static long serialVersionUID = 1;

    private DocumentParagraphDiff differences;

    private ParagraphIdentifier paragraphID;

    public EditDocChannelData(ParagraphIdentifier paragraphID, DocumentParagraphDiff differences,
            UserName creator, Date timestamp) {
        super(creator, timestamp, DocumentChannelDataType.EDIT);

        this.differences = differences;
        this.paragraphID = paragraphID;
    }

    public DocumentParagraphDiff getDifferences() {
        return differences;
    }

    public void setDifferences(DocumentParagraphDiff differences) {
        this.differences = differences;
    }

    public ParagraphIdentifier getParagraphID() {
        return paragraphID;
    }

    public void setParagraphID(ParagraphIdentifier paragraphID) {
        this.paragraphID = paragraphID;
    }

}
