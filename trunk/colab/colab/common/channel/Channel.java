package colab.common.channel;

import colab.common.identity.Identifiable;
import colab.common.naming.ChannelName;

/**
 * Represents a channel in the system, including data,
 * usernames and metadata.
 *
 * Each document or collaborative entity is a channel.
 * A channel represents content being edited, and the
 * workspace in which the group is dealing with it.
 */
public interface Channel extends Identifiable<ChannelName> {

    /**
     * @return a descriptor object with basic information about this channel
     */
    ChannelDescriptor getChannelDescriptor();

}