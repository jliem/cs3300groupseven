package colab.common.channel;

import java.util.Date;

import colab.common.naming.UserName;
import colab.common.xml.XmlNode;

public class DocumentChannelData extends ChannelData {

    public enum DocumentChannelDataType {
        INSERT,
        DELETE,
        EDIT
    }
    
    public static final long serialVersionUID = 1;
    
    private DocumentChannelDataType type;
    
    
    
    public DocumentChannelData(ChannelDataIdentifier id, UserName creator, Date timestamp, DocumentChannelDataType type) {
        super(id, creator, timestamp);
        this.type = type;
    }

    public XmlNode toXml() {
        // TODO Auto-generated method stub
        return null;
    }

}