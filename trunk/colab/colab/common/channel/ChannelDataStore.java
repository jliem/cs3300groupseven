package colab.common.channel;

import java.util.List;

public interface ChannelDataStore<T extends ChannelData> {

    void add(T data);

    /**
     * Returns the last n data items.  If the count is negative or
     * greater than the number of available items, all data is returned.
     */
    List<T> getLast(final int count);

    List<T> getAll();

    ChannelDataIdentifier getNextDataId();

}
