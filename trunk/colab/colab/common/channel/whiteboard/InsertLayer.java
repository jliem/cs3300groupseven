package colab.common.channel.whiteboard;

import java.util.Date;

import colab.common.channel.ChannelDataIdentifier;
import colab.common.channel.whiteboard.layer.Layer;
import colab.common.channel.whiteboard.layer.LayerIdentifier;
import colab.common.exception.NotApplicableException;
import colab.common.naming.UserName;
import colab.common.util.StringUtils;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;

/**
 * A command to insert a new blank layer into a whiteboard.
 */
public class InsertLayer extends WhiteboardChannelData {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private Layer layer;

    private LayerIdentifier previous;

    /**
     * Constructs an empty InsertLayer.
     */
    public InsertLayer() {
    }

    /**
     * Constructs a new InsertLayer.
     *
     * @param creator the user who drew the figure
     * @param timestamp the time at which the figure was drawn
     * @param previous the id of another layer in the whiteboard,
     *                after which this one should be inserted
     */
    public InsertLayer(final UserName creator, final Date timestamp,
            final LayerIdentifier previous) {

        super(creator, timestamp, null);

        this.previous = previous;
        this.layer = new Layer(new LayerIdentifier((Integer) null));

    }

    /**
     * @return the id of the previous layer in the whiteboard
     */
    public LayerIdentifier getPrevious() {
        return previous;
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Insert";
    }

    /** {@inheritDoc} */
    public void apply(final Whiteboard whiteboard)
            throws NotApplicableException {
        whiteboard.insert(getPrevious(), layer);
    }

    /**
     * @return the inserted layer
     */
    public Layer getLayer() {
        return layer;
    }

    /**
     * @param layer the inserted layer
     */
    public void setLayer(final Layer layer) {
        this.layer = layer;
    }

    /**
     * @return a deep copy
     */
    public InsertLayer copy() {

        UserName username = null;
        if (super.getCreator() != null) {
            username = new UserName(super.getCreator().getValue());
        }

        LayerIdentifier li = null;
        if (getLayerId() != null) {
            li = new LayerIdentifier(getLayerId().getValue());
        }

        InsertLayer copy = new InsertLayer(username, super.getTimestamp(), li);

        if (layer != null) {
            copy.setLayer(layer.copy());
        }

        // Set channel id
        copy.setId(this.getId());

        // Set previous
        if (previous != null) {
            copy.previous = new LayerIdentifier(previous);
        } else {
            copy.previous = null;
        }

        return copy;
    }

    /** {@inheritDoc} */
    @Override
    public XmlNode toXml() {

        XmlNode node = super.toXml();

        String prevStr = "";
        if (previous != null) {
            prevStr = previous.toString();
        }
        node.setAttribute("previous", prevStr);

        return node;

    }

    /** {@inheritDoc} */
    @Override
    public void fromXml(final XmlNode node) throws XmlParseException {

        super.fromXml(node);

        String prevStr = node.getAttribute("previous");
        if (!StringUtils.isEmptyOrNull(prevStr)) {
            try {
                this.previous = new LayerIdentifier(
                        Integer.parseInt(prevStr));
            } catch (final NumberFormatException e) {
                throw new XmlParseException(e);
            }
        }

        this.layer = new Layer(new LayerIdentifier(this.getId()));

    }

    /** {@inheritDoc} */
    @Override
    public void setId(final ChannelDataIdentifier id) {

        super.setId(id);

        LayerIdentifier layerId;
        if (id != null) {
            layerId = new LayerIdentifier(id);
        } else {
            layerId = null;
        }
        this.setLayerId(layerId);

    }

}
