package colab.common.channel.document;

import java.util.Date;

import colab.common.channel.ChannelData;
import colab.common.exception.NotApplicableException;
import colab.common.naming.UserName;
import colab.common.xml.XmlConstructor;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;
import colab.common.xml.XmlSerializable;
import colab.server.channel.ServerDocumentChannel;

/**
 * The ChannelData flavor for document editing functionality.
 *
 * @see ServerDocumentChannel
 * @see DocumentParagraph
 */
public abstract class DocumentChannelData extends ChannelData
        implements XmlSerializable {

    protected enum DocumentChannelDataType {
        INSERT,
        DELETE,
        EDIT
    }

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private DocumentChannelDataType type;

    protected DocumentChannelData() {
    }

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

    /**
     * @return an XmlConstructor for DocumentChannelData.
     */
    public static XmlConstructor<DocumentChannelData> getXmlConstructor() {
        return XML_CONSTRUCTOR;
    }

    private static final XmlConstructor<DocumentChannelData> XML_CONSTRUCTOR =
        new XmlConstructor<DocumentChannelData>() {
            public DocumentChannelData fromXml(final XmlNode node)
                    throws XmlParseException {
                return constructFromXml(node);
            }
        };

    private static DocumentChannelData constructFromXml(final XmlNode node)
            throws XmlParseException {

        DocumentChannelData data;
        String type = node.getType();
        if (type.equals("Insert")) {
            data = new InsertDocChannelData();
        } else if (type.equals("Edit")) {
            data = new EditDocChannelData();
        } else if (type.equals("Delete")) {
            data = new DeleteDocChannelData();
        } else {
            throw new XmlParseException();
        }

        data.fromXml(node);

        return data;

    }

}
