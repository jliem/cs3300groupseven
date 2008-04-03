package colab.common.channel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * A set of ChannelData, sorted by their identifiers.
 *
 * @param <T> the type of channel data in the set
 */
public final class ChannelDataSet<T extends ChannelData>
        implements ChannelDataStore<T> {

    /**
     * An integer which is always higher than the highest
     * identifier value in the set, used to assign an
     * identifier to the next piece of data that is added.
     */
    private Integer nextDataId = 1;

    /**
     * The data elements represented by this set.
     */
    private final TreeSet<T> dataSet;

    /**
     * Constructs an empty ChannelDataSet.
     */
    public ChannelDataSet() {

        dataSet = new TreeSet<T>(
                new Comparator<T>() {
            public int compare(final T arg0, final T arg1) {
                return arg1.getId().compareTo(arg0.getId());
            }
        });

    }

    /** {@inheritDoc} */
    public void add(final T data) {

        if (data.getId() == null) {
            // data's identifier is null, so assign it one
            data.setId(new ChannelDataIdentifier(nextDataId));
        }

        Integer id = data.getId().getValue();

        // Ensure that nextDataId is still greater than every id
        if (id >= nextDataId) {
            nextDataId = id + 1;
        }

        dataSet.add(data);

    }

    /** {@inheritDoc} */
    public void addAndAssignId(final T data) {
        data.setId(null);
        add(data);
    }

    /** {@inheritDoc} */
    public int size() {

        return dataSet.size();

    }

    /** {@inheritDoc} */
    public List<T> getLast(final int requestedItems) {

        int count = requestedItems;

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

    /** {@inheritDoc} */
    public List<T> getAll() {

        return getLast(-1);

    }

}
