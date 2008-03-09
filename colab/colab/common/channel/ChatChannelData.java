package colab.common.channel;

import java.util.Date;

import colab.common.naming.UserName;
import colab.common.xml.XmlNode;

/**
 * Represents a message posted to a chat channel.
 */
public final class ChatChannelData extends ChannelData {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /** The text of the message. */
    private String text;

    /**
     * Constructs a new chat data object.
     *
     * @param text the text of the message
     * @param creator the user who posted the message
     */
    public ChatChannelData(final String text, final UserName creator) {
        super(creator, new Date());
        this.text = text;
    }

    /**
     * Returns the contents of the message.
     *
     * @return the textual message
     */
    public String getText() {
        return this.text;
    }

    // TODO: needs some sort of formatting options
    public String getMessageString(final boolean timestampEnabled) {
        String start = getCreator().toString();
        String end = ": " + getText();
        if(timestampEnabled){
            start += " <" + getTimestamp().toString() + "> ";
        }
        return start + end;
    }

    public XmlNode toXml() {
        XmlNode node = new XmlNode("ChatMessage");
        node.setAttribute("time", getTimestamp().toString());
        node.setAttribute("creator", getCreator().toString());
        node.setContent(text);
        return node;
    }

}
