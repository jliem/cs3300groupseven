package colab.common.channel;

import java.util.Date;

import colab.common.user.UserName;

/**
 * Represents some piece of data in a Channel.
 */
public abstract class ChannelData {

    /**
     * The user from whom this piece of data originated.
     */
    private final UserName creator;

    /**
     * The time at which the data was originally created.
     */
    private final Date timestamp;

    /**
     * Constructs a new channel data object.
     *
     * @param creator the user who created it
     * @param timestamp the time at which it was created
     */
    public ChannelData(final UserName creator, final Date timestamp) {
        this.creator = creator;
        this.timestamp = timestamp;
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
     * Returns the creation datetime of this data.
     *
     * @return the time at which this data was originally created
     */
    public final Date getTimestamp() {
        return this.timestamp;
    }

}
