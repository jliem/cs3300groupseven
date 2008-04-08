package colab.common.channel.document;

import colab.common.exception.NotApplicableException;
import colab.common.identity.ParagraphIdentifier;
import colab.common.naming.UserName;

public class LockDocChannelData extends DocumentChannelData {

    public static final long serialVersionUID = 1;
    
    private UserName lockHolder;
    
    private ParagraphIdentifier id;
    
    public LockDocChannelData(UserName lockHolder, ParagraphIdentifier id) {
        this.id = id;
        this.lockHolder = lockHolder;
    }
    
    @Override
    public void apply(Document doc) throws NotApplicableException {
        DocumentParagraph para = doc.get(id);
        
        if(para == null) {
            throw new NotApplicableException("The document has no matching paragraph.");
        }
        
        if(para.isLocked() && lockHolder != null) {
            throw new NotApplicableException("A locked paragraph must be unlocked before the lock owner may be changed.");
        }
        
        if(lockHolder == null) {
            para.unlock();
        }
        else {
            para.lock(lockHolder);
        }
    }

    public String xmlNodeName() {
        // TODO Auto-generated method stub
        return null;
    }

    
}
