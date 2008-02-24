package colab.common.channel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public final class ChatDataCollection extends TreeSet<ChatChannelData> {

    public ChatDataCollection() {
        super(new Comparator<ChatChannelData>() {
            public int compare(final ChatChannelData arg0,
                    final ChatChannelData arg1) {
                return arg0.getTimestamp().compareTo(arg1.getTimestamp());
            }
        });
    }

    public List<ChannelData> getLast(final int count) {
        Iterator<ChatChannelData> it = descendingIterator();
        List<ChannelData> result = new ArrayList<ChannelData>(count);
        while (it.hasNext()) {
            result.add(0, it.next());
        }
        return result;
    }

}
