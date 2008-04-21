package colab.common.channel.whiteboard;

import java.util.Date;

import colab.common.channel.whiteboard.draw.Figure;
import colab.common.channel.whiteboard.layer.LayerIdentifier;
import colab.common.exception.NotApplicableException;
import colab.common.naming.UserName;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;

public class EditLayer extends WhiteboardChannelData {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private Figure figure;

    public EditLayer(final UserName creator, final Date timestamp,
            final LayerIdentifier layerId,
            final Figure figure) {

        super(creator, timestamp, layerId);

        this.figure = figure;
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Edit";
    }

    public XmlNode toXml() {
        XmlNode node = super.toXml();

        node.addChild(figure.toXml());

        return node;
    }

    public void fromXml(XmlNode node) throws XmlParseException {
        super.fromXml(node);

        figure = Figure.constructFromXml(node);

    }

    /** {@inheritDoc} */
    public void apply(Whiteboard whiteboard) throws NotApplicableException {
        whiteboard.addFigure(layerId, figure);
    }

}

