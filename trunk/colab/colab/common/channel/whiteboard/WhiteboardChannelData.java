package colab.common.channel.whiteboard;

import java.util.Date;

import colab.common.channel.ChannelData;
import colab.common.channel.whiteboard.layer.LayerIdentifier;
import colab.common.exception.NotApplicableException;
import colab.common.naming.UserName;
import colab.common.xml.XmlConstructor;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;

/**
 * Represents a revision to a whiteboard channel.
 */
public abstract class WhiteboardChannelData extends ChannelData {

    private LayerIdentifier layerId;

    /**
     * Constructs an empty WhiteboardChannelData.
     */
    protected WhiteboardChannelData() {
    }

    protected WhiteboardChannelData(final UserName creator,
            final Date timestamp, final LayerIdentifier layerId) {
        super(creator, timestamp);
        this.layerId = layerId;
    }

    /**
     * @return the id of the layer being edited
     */
    public LayerIdentifier getLayerId() {
        return layerId;
    }

    /**
     * @param layerId the id of the layer being edited
     */
    public void setLayerId(final LayerIdentifier layerId) {
        this.layerId = layerId;
    }

    /**
     * Applies this data to a whiteboard.
     * @param whiteboard the whiteboard to use.
     * @throws NotApplicableException if the data could not be applied
     */
    public abstract void apply(final Whiteboard whiteboard)
        throws NotApplicableException;

    /** {@inheritDoc} */
    @Override
    public XmlNode toXml() {

        XmlNode node = super.toXml();

        node.setAttribute("layerId", this.layerId.toString());

        return node;

    }

    /** {@inheritDoc} */
    @Override
    public void fromXml(final XmlNode node) throws XmlParseException {

        super.fromXml(node);

        try {
            this.layerId = new LayerIdentifier(
                    Integer.parseInt(node.getAttribute("layerId")));
        } catch (final NumberFormatException e) {
            throw new XmlParseException(e);
        }

    }

    public static XmlConstructor<WhiteboardChannelData> getXmlConstructor() {
        return XML_CONSTRUCTOR;
    }

    private static final XmlConstructor<WhiteboardChannelData> XML_CONSTRUCTOR =
        new XmlConstructor<WhiteboardChannelData>() {
            public WhiteboardChannelData fromXml(final XmlNode node)
                    throws XmlParseException {
                return constructFromXml(node);
            }
        };

    private static WhiteboardChannelData constructFromXml(final XmlNode node)
            throws XmlParseException {

        WhiteboardChannelData data = instantiateFromXmlType(node.getType());
        data.fromXml(node);
        return data;

    }

    private static WhiteboardChannelData instantiateFromXmlType(
            final String type) throws XmlParseException {

        WhiteboardChannelData data;

        //Delete, edit, insert, lock, move
        data = new DeleteLayer();
        if (type.equals(data.xmlNodeName())) {
            return data;
        }

        data = new EditLayer();
        if (type.equals(data.xmlNodeName())) {
            return data;
        }

        data = new InsertLayer();
        if (type.equals(data.xmlNodeName())) {
            return data;
        }

        data = new LockLayer();
        if (type.equals(data.xmlNodeName())) {
            return data;
        }

        data = new MoveLayer();
        if (type.equals(data.xmlNodeName())) {
            return data;
        }

        throw new XmlParseException();

    }

}
