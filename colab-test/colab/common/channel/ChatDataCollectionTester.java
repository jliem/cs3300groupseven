package colab.common.channel;

import java.util.List;

import junit.framework.TestCase;
import colab.common.naming.UserName;

public class ChatDataCollectionTester extends TestCase {

    public void testGetLast() {
        ChatDataCollection col = new ChatDataCollection();
        UserName user = new UserName("Johannes");

        ChatChannelData[] data = new ChatChannelData[5];

        for (int i=0; i<data.length; i++)
            data[0] = new ChatChannelData(""+i, user);

        col.add(data[0]);

        List<ChatChannelData> list = col.getLast(0);

        assertEquals(list.size(), 0);

        list = col.getLast(1);

        assertEquals(list.size(), 1);

        assertEquals(list.get(0), data[0]);

        for (int i=1; i<data.length; i++) {
            col.add(data[i]);
        }

        list = col.getLast(data.length);

        for (int i=1; i<data.length; i++) {
            col.add(data[i]);
        }

        // Verify that we pull them out in the same order
        for (int i=0; i<data.length; i++) {
            assertEquals(data[i], list.get(i));
        }
    }
}
