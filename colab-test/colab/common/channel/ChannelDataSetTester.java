package colab.common.channel;

import colab.common.naming.UserName;
import junit.framework.TestCase;

public class ChannelDataSetTester extends TestCase {

    /**
     * Adds several pieces of chat data to a set
     * and ensures that the size is correct.
     */
    public void testAddAndSize() throws Exception {

        ChannelDataSet<ChatChannelData> set =
            new ChannelDataSet<ChatChannelData>();

        set.add(new ChatChannelData(
                "Message one", new UserName("Chris")));
        Thread.sleep(50);
        set.add(new ChatChannelData(
                "Message two", new UserName("Chris")));
        Thread.sleep(50);
        set.add(new ChatChannelData(
                "Message three", new UserName("Pamela")));

        assertEquals(3, set.size());

    }

}
