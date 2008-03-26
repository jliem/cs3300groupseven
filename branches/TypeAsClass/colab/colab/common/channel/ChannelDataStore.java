package colab.common.channel;

import java.util.List;

/**
 * A collection of data in a channel.
 *
 * @param <T> the type of data in the collection
 */
public interface ChannelDataStore<T extends ChannelData> {

    /**
     * Adds one data item to the store.
     *
     * If the data object's identifier is null,
     * a new identifier will be assigned to it.
     *
     * @param data the data item to add
     */
    void add(T data);

    /**
     * Returns the last n data items.  If the count is negative or
     * greater than the number of available items, all data is returned.
     *
     * @param count the number of items to retrieve
     * @return the last n data items
     */
    List<T> getLast(final int count);

    /**
     * @return all of the data items contained in this store
     */
    List<T> getAll();

    /**
     * @return the number of data items
     */
    int size();

}
