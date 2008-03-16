package colab.common.channel;

import colab.common.naming.UserName;
import junit.framework.TestCase;

public final class ChannelDataSetTester extends TestCase {

    /**
     * Adds several pieces of chat data to a set
     * and ensures that the size is correct.
     *
     * @throws Exception if any exception is thrown
     */
    public void testAddAndSize() throws Exception {

        ChannelDataSet<ChatChannelData> set =
            new ChannelDataSet<ChatChannelData>();

        set.add(new ChatChannelData(
                new ChannelDataIdentifier(1),
                "Message one",
                new UserName("Chris")));

        Thread.sleep(50);

        set.add(new ChatChannelData(
                new ChannelDataIdentifier(2),
                "Message two",
                new UserName("Chris")));

        Thread.sleep(50);

        set.add(new ChatChannelData(
                new ChannelDataIdentifier(3),
                "Message three",
                new UserName("Pamela")));

        assertEquals(3, set.size());

    }

}
