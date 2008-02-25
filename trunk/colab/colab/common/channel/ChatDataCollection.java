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
                return arg1.getTimestamp().compareTo(arg0.getTimestamp());
            }
        });
    }

    public List<ChatChannelData> getLast(final int count) {
        Iterator<ChatChannelData> it = iterator();
        List<ChatChannelData> result = new ArrayList<ChatChannelData>(count);
        while (it.hasNext()) {
            result.add(0, it.next());
        }
        return result;
    }

}
