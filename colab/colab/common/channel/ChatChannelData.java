package colab.common.channel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import colab.common.naming.UserName;
import colab.common.xml.XmlConstructor;
import colab.common.xml.XmlNode;

/**
 * Represents a message posted to a chat channel.
 */
public final class ChatChannelData extends ChannelData {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /** The text of the message. */
    private String text;

    private static final DateFormat DATE_FORMAT =
        new SimpleDateFormat("d MMM yyyy HH:mm:ss.S");

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
     * Constructs a new chat data object.
     *
     * @param text the text of the message
     * @param creator the user who posted the message
     */
    public ChatChannelData(final String text, final UserName creator,
            final Date time) {
        super(creator, time);
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
        node.setAttribute("time", DATE_FORMAT.format(getTimestamp()));
        node.setAttribute("creator", getCreator().toString());
        node.setContent(text);
        return node;
    }

    private static XmlConstructor<ChatChannelData> XML_CONSTRUCTOR =
            new XmlConstructor<ChatChannelData>() {
        public ChatChannelData fromXml(final XmlNode node)
                throws ParseException {
            Date time = DATE_FORMAT.parse(node.getAttribute("time"));
            UserName creator = new UserName(node.getAttribute("creator"));
            String message = node.getBody();
            ChatChannelData data = new ChatChannelData(message, creator, time);
            return data;
        }
    };

    public static XmlConstructor<ChatChannelData> getXmlConstructor() {
        return XML_CONSTRUCTOR;
    }

    public boolean equals(final Object obj) {
        if (!(obj instanceof ChatChannelData)) {

        }
        ChatChannelData otherData = (ChatChannelData) obj;
        return otherData.getCreator().equals(getCreator())
            && otherData.getTimestamp().equals(getTimestamp())
            && otherData.getText().equals(getText());
    }

}
