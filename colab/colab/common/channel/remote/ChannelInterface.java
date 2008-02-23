package colab.common.channel.remote;

import java.rmi.Remote;

/**
 * A remote object passed to the server, so that
 * the server can send messages to the client.
 */
public interface ChannelInterface extends Remote {

}
