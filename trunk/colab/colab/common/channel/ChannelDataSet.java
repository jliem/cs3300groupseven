package colab.common.channel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class ChannelDataSet<T extends ChannelData>
        implements ChannelDataStore<T> {

    final protected TreeSet<T> dataSet;

    public ChannelDataSet() {

        dataSet = new TreeSet<T>(
                new Comparator<T>() {
            public int compare(final T arg0, final T arg1) {
                return arg1.getTimestamp().compareTo(arg0.getTimestamp());
            }
        });

    }

    public void add(final T data) {

        dataSet.add(data);

    }

    public int size() {

        return dataSet.size();

    }

    /**
     * Returns the last n data items.  If the count is negative or
     * greater than the number of available items, all data is returned.
     */
    public List<T> getLast(int count) {

        if (count < 0) {
            count = Integer.MAX_VALUE;
        }

        Iterator<T> it = dataSet.iterator();
        List<T> result = new ArrayList<T>();
        while (it.hasNext() && count-- > 0) {
            result.add(0, it.next());
        }
        return result;

    }

    public List<T> getAll() {

        return getLast(-1);

    }

}
