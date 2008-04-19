package colab.common.channel.whiteboard;

public class MoveLayer extends WhiteboardChannelData {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Move";
    }

}
