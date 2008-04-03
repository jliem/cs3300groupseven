package colab.common.channel.document;

import java.text.ParseException;
import java.util.Date;

import colab.common.channel.ChannelData;
import colab.common.exception.NotApplicableException;
import colab.common.naming.UserName;
import colab.common.xml.XmlConstructor;
import colab.common.xml.XmlNode;
import colab.server.channel.ServerDocumentChannel;

/**
 * The ChannelData flavor for document editing functionality.
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

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private DocumentChannelDataType type;

    protected DocumentChannelData(final UserName creator, final Date timestamp,
            final DocumentChannelDataType type) {

        super(creator, timestamp);
        this.type = type;

    }

    public final DocumentChannelDataType getType() {

        return type;

    }

    public abstract void apply(final Document doc)
            throws NotApplicableException;

    public XmlNode toXml() {

        return null; // TODO:

    }

    /**
     * @return an XmlConstructor for DocumentChannelData.
     */
    public static XmlConstructor<DocumentChannelData> getXmlConstructor() {
        return XML_CONSTRUCTOR;
    }

   private static final XmlConstructor<DocumentChannelData> XML_CONSTRUCTOR =
           new XmlConstructor<DocumentChannelData>() {

       public DocumentChannelData fromXml(final XmlNode node)
               throws ParseException {

           /*
           ChannelDataIdentifier id = new ChannelDataIdentifier(
                   Integer.parseInt(node.getAttribute("id")));
           Date time = DATE_FORMAT.parse(node.getAttribute("time"));
           UserName creator = new UserName(node.getAttribute("creator"));
           String message = node.getBody();
           return new DocumentChannelData(id, message, creator, time);
           */

           return null; // TODO:

       }

   };

}
