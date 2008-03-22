package colab.common.channel;

import java.util.Date;

import colab.common.identity.ParagraphIdentifier;
import colab.common.naming.UserName;

public class DeleteDocChannelData extends DocumentChannelData {
    
    private final static long serialVersionUID = 1;
    
    private ParagraphIdentifier paragraphID;
    
    public DeleteDocChannelData(ParagraphIdentifier paragraphID, UserName creator, Date timestamp) {
        super(creator, timestamp, DocumentChannelDataType.DELETE);
       
        this.paragraphID = paragraphID;
    }

    public ParagraphIdentifier getParagraphID() {
        return paragraphID;
    }

    public void setParagraphID(ParagraphIdentifier paragraphID) {
        this.paragraphID = paragraphID;
    }
    
    
}
