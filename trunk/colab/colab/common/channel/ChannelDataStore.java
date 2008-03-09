package colab.common.channel;

import java.util.List;

public interface ChannelDataStore<T extends ChannelData> {

    void add(T data);

    List<T> getLast(final int count);

    List<T> getAll();

}
