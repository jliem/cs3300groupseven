package colab.common.channel;

import java.util.Date;

import colab.common.Document;
import colab.common.DocumentParagraph;
import colab.common.identity.ParagraphIdentifier;
import colab.common.naming.UserName;
import colab.common.xml.XmlNode;
import colab.server.channel.ServerDocumentChannel;


/**
 * @author matt The ChannelData flavor for document editing functionality.
 *
 * This class, through its subclasses, represents a few more architectural
 * problems we've got on our plate- namely, that of wide-spread synchronization.
 * The most obvious issue is this- if I want to insert a paragraph, and I do
 * that using an index, how do I know that my index is correct? We can't get
 * locks on the whole paragraph structure... I've temporarily solved the most
 * obvious aspects of the problem with {@link ParagraphIdentifier}, but that
 * really doesn't cut it, since it has its own drawbacks.
 *
 * @see ServerDocumentChannel
 * @see DocumentParagraph
 */
public abstract class DocumentChannelData extends ChannelData {

    public enum DocumentChannelDataType {
        INSERT,
        DELETE,
        EDIT
    }

    public static final long serialVersionUID = 1;

    private DocumentChannelDataType type;

    protected DocumentChannelData(final UserName creator, final Date timestamp,
            final DocumentChannelDataType type) {
        super(creator, timestamp);
        this.type = type;
    }

    public final DocumentChannelDataType getType() {
        return type;
    }

    public abstract void apply(final Document doc) throws Exception;

    public XmlNode toXml() {
        // TODO: Auto-generated method stub
        return null;
    }

    /**
     * A closure which constructs a ChatChannelData from an xml node.
     */
    /*private static final XmlConstructor<DocumentChannelData> XML_CONSTRUCTOR =
            new XmlConstructor<DocumentChannelData>() {
        public ChatChannelData fromXml(final XmlNode node)
                throws ParseException {
            ChannelDataIdentifier id = new ChannelDataIdentifier(
                    Integer.parseInt(node.getAttribute("id")));
            Date time = DATE_FORMAT.parse(node.getAttribute("time"));
            UserName creator = new UserName(node.getAttribute("creator"));
            String message = node.getBody();
            return new ChatChannelData(id, message, creator, time);
        }
    };
*/
    /**
     * @return an XmlConstructor for ChatChannelData.
     */
    /*public static XmlConstructor<ChatChannelData> getXmlConstructor() {
        return XML_CONSTRUCTOR;
    }*/

   /*private static final XmlConstructor<DocumentChannelData> XML_CONSTRUCTOR =
           new XmlConstructor<DocumentChannelData>() {
       public DocumentChannelData fromXml(final XmlNode node)
               throws ParseException {
           ChannelDataIdentifier id = new ChannelDataIdentifier(
                   Integer.parseInt(node.getAttribute("id")));
           Date time = DATE_FORMAT.parse(node.getAttribute("time"));
           UserName creator = new UserName(node.getAttribute("creator"));
           String message = node.getBody();
           return new DocumentChannelData(id, message, creator, time);
       }
   };*/

}
