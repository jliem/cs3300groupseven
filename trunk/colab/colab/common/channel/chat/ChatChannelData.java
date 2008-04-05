package colab.common.channel.chat;

import java.text.ParseException;
import java.util.Date;

import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDataIdentifier;
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

    /**
     * Constructs an empty ChatChannelData.
     */
    public ChatChannelData() {
    }

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
     * @param id the id of the data object
     * @param text the text of the message
     * @param creator the user who posted the message
     */
    public ChatChannelData(final ChannelDataIdentifier id,
            final String text, final UserName creator) {
        super(id, creator, new Date());
        this.text = text;
    }

    /**
     * Constructs a new chat data object.
     *
     * @param id the id of the data object
     * @param text the text of the message
     * @param creator the user who posted the message
     * @param time the time at which this data was posted
     */
    public ChatChannelData(final ChannelDataIdentifier id,
            final String text, final UserName creator, final Date time) {
        super(id, creator, time);
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

    /**
     * Returns the message with additional formatting applied.
     *
     * @param timestampEnabled whether to include the timestamp
     * @return a human-readable message string
     */
    public String getMessageString(final boolean timestampEnabled) {
        String start = getCreator().toString();
        String end = ": " + getText();
        if(timestampEnabled){
            start += " <" + getTimestamp().toString() + "> ";
        }
        return start + end;
    }

    public static XmlConstructor<ChatChannelData> getXmlConstructor() {
        return (new XmlConstructor<ChatChannelData>() {
            public ChatChannelData fromXml(final XmlNode node)
                    throws ParseException {
                ChatChannelData data = new ChatChannelData();
                data.fromXml(node);
                return data;
            }
        });
    }

    /** {@inheritDoc} */
    public XmlNode toXml() {
        XmlNode node = new XmlNode("ChatMessage");
        node.setAttribute("id", getId().toString());
        node.setAttribute("time", DATE_FORMAT.format(getTimestamp()));
        node.setAttribute("creator", getCreator().toString());
        node.setContent(text);
        return node;
    }

    /** {@inheritDoc} */
    public void fromXml(final XmlNode node) throws ParseException {
        setId(new ChannelDataIdentifier(
                Integer.parseInt(node.getAttribute("id"))));
        setTimestamp(DATE_FORMAT.parse(node.getAttribute("time")));
        setCreator(new UserName(node.getAttribute("creator")));
        this.text = node.getBody();
    };

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof ChatChannelData)) {
            return false;
        }
        ChatChannelData otherData = (ChatChannelData) obj;
        return otherData.getCreator().equals(getCreator())
            && otherData.getTimestamp().equals(getTimestamp())
            && otherData.getText().equals(getText());
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return getCreator().hashCode()
             + getTimestamp().hashCode()
             + getText().hashCode();
    }

}
