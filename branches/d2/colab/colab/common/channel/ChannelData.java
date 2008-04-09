package colab.common.channel;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import colab.common.identity.Identifiable;
import colab.common.naming.UserName;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;
import colab.common.xml.XmlSerializable;

/**
 * Represents some piece of data in a Channel.
 */
public abstract class ChannelData implements Serializable,
        Identifiable<ChannelDataIdentifier>, XmlSerializable {

    /** The date format used to serialize the timestamp. */
    protected static final DateFormat DATE_FORMAT =
        new SimpleDateFormat("d MMM yyyy HH:mm:ss.S");

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /** The id of this piece of date (unique to its respective channel). */
    private ChannelDataIdentifier id;

    /**
     * The user from whom this piece of data originated.
     */
    private UserName creator;

    /**
     * The time at which the data was originally created.
     */
    private Date timestamp;

    /**
     * Constructs a new channel object.
     */
    public ChannelData() {
        this.id = null;
        this.creator = null;
        this.timestamp = null;
    }

    /**
     * Constructs a new channel data object.
     *
     * @param creator the user who created it
     * @param timestamp the time at which it was created
     */
    public ChannelData(final UserName creator, final Date timestamp) {
        this.id = null;
        this.creator = creator;
        this.timestamp = timestamp;
    }

    /**
     * Constructs a new channel data object.
     *
     * @param id the identifier of the data
     * @param creator the user who created it
     * @param timestamp the time at which it was created
     */
    public ChannelData(final ChannelDataIdentifier id,
            final UserName creator, final Date timestamp) {
        this.id = id;
        this.creator = creator;
        this.timestamp = timestamp;
    }

    /**
     * Retrieves the identifier for this object.
     *
     * @return the identifier for this channel data.
     */
    public final ChannelDataIdentifier getId() {
        return this.id;
    }

    /**
     * Sets the identifier for this object.
     *
     * @param id the identifier for this channel data.
     */
    public final void setId(final ChannelDataIdentifier id) {
        this.id = id;
    }

    /**
     * Returns the user who created this data.
     *
     * @return the user from whom this data originated
     */
    public final UserName getCreator() {
        return this.creator;
    }

    /**
     * Sets the user who created this data.
     *
     * @param creator the user from whom this data originated
     */
    public final void setCreator(final UserName creator) {
        this.creator = creator;
    }

    /**
     * Returns the creation datetime of this data.
     *
     * @return the time at which this data was originally created
     */
    public final Date getTimestamp() {
        return this.timestamp;
    }

    /**
     * Sets the creation datetime of this data.
     *
     * @param timestamp the time at which this data was originally created
     */
    public final void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Sets the creation datetime of this data as the current time.
     *
     * Equivalent to setTimestamp(new Date()).
     */
    public final void setTimestamp() {
        this.timestamp = new Date();
    }

    /** {@inheritDoc} */
    public XmlNode toXml() {

        XmlNode node = new XmlNode(xmlNodeName());

        node.setAttribute("id", this.id.toString());
        node.setAttribute("time", DATE_FORMAT.format(this.timestamp));
        node.setAttribute("creator", this.creator.toString());

        return node;

    }

    /** {@inheritDoc} */
    public void fromXml(final XmlNode node) throws XmlParseException {

        try {
            int intId = Integer.parseInt(node.getAttribute("id"));
            this.id = new ChannelDataIdentifier(intId);
        } catch (final NumberFormatException e) {
            throw new XmlParseException(e);
        }

        try {
            this.timestamp = DATE_FORMAT.parse(node.getAttribute("time"));
        } catch (final ParseException e) {
            throw new XmlParseException(e);
        }

        this.creator = new UserName(node.getAttribute("creator"));

    }

}
