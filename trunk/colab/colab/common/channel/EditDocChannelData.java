package colab.common.channel;

import java.util.Date;

import colab.common.Document;
import colab.common.DocumentParagraph;
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

    @Override
    public void apply(Document doc) throws Exception {
        doc.applyEdit(paragraphID, differences);
    }
    
    public void apply(DocumentParagraph para) {
        differences.apply(para);
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
