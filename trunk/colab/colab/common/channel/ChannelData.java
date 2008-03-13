package colab.common.channel;

import java.io.Serializable;
import java.util.Date;

import colab.common.identity.Identifiable;
import colab.common.naming.UserName;
import colab.common.xml.XmlNode;

/**
 * Represents some piece of data in a Channel.
 */
public abstract class ChannelData implements Serializable,
        Identifiable<ChannelDataIdentifier> {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

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

    public final ChannelDataIdentifier getId() {
        return this.id;
    }

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

    public final void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }

    public final void setTimestamp() {
        this.timestamp = new Date();
    }

    /**
     * @return an xml node containing all of the data required
     *         to serialize this object.
     */
    public abstract XmlNode toXml();

}
