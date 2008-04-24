package colab.common.channel.whiteboard;

import java.util.Date;

import colab.common.channel.whiteboard.draw.Figure;
import colab.common.channel.whiteboard.layer.LayerIdentifier;
import colab.common.exception.NotApplicableException;
import colab.common.naming.UserName;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;

/**
 * An edit to a layer consists of adding a figure.
 */
public class EditLayer extends WhiteboardChannelData {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private Figure figure;

    /**
     * Constructs an empty EditLayer.
     */
    public EditLayer() {
    }

    /**
     * Constructs a new EditLayer.
     *
     * @param creator the user who drew the figure
     * @param timestamp the time at which the figure was drawn
     * @param layerId the id of the layer being edited
     * @param figure the figure which was drawn
     */
    public EditLayer(final UserName creator, final Date timestamp,
            final LayerIdentifier layerId, final Figure figure) {

        super(creator, timestamp, layerId);

        this.figure = figure;
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Edit";
    }

    /** {@inheritDoc} */
    @Override
    public XmlNode toXml() {

        XmlNode node = super.toXml();

        node.addChild(figure.toXml());

        return node;

    }

    /** {@inheritDoc} */
    @Override
    public void fromXml(final XmlNode node) throws XmlParseException {

        super.fromXml(node);

        figure = Figure.constructFromXml(node.getChildren().get(0));

    }

    /** {@inheritDoc} */
    public void apply(final Whiteboard whiteboard)
            throws NotApplicableException {

        whiteboard.addFigure(getLayerId(), figure);

    }

}

