package colab.server.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import colab.common.channel.ChannelDataIdentifier;
import colab.common.channel.ChatChannelData;
import colab.common.naming.UserName;

/**
 * Test cases for {@link ChannelFile}.
 */
public final class ChannelFileTester extends TestCase {

    /**
     * Generates some chat data, writes it to a temporary
     * xml file, then parses the xml to recover the data.
     *
     * The message data contains characters which need to
     * be escaped, as well as escape strings, in attempt
     * to trip up the writer or parser.
     *
     * @throws Exception if any exception is thrown
     */
    public void testChatChannel() throws Exception {

        File file = File.createTempFile("colabTestChatChannelFile", null);
        ChannelFile<ChatChannelData> channelFile =
            new ChannelFile<ChatChannelData>(
                    file, ChatChannelData.getXmlConstructor());

        List<ChatChannelData> outData = new ArrayList<ChatChannelData>();
        outData.add(new ChatChannelData(
                new ChannelDataIdentifier(4),
                "Message one",
                new UserName("Chris")));
        Thread.sleep(50);
        outData.add(new ChatChannelData(
                new ChannelDataIdentifier(9),
                "Message two & things...",
                new UserName("Chris")));
        Thread.sleep(50);
        outData.add(new ChatChannelData(
                new ChannelDataIdentifier(10),
                "&&&amp;<&lt;;&",
                new UserName("Pamela")));
        Thread.sleep(50);
        outData.add(new ChatChannelData(
                new ChannelDataIdentifier(16),
                "Message\r\nfour</message>",
                new UserName("Chris")));

        for (final ChatChannelData message : outData) {
            channelFile.add(message);
        }

        channelFile = new ChannelFile<ChatChannelData>(
                    file, ChatChannelData.getXmlConstructor());

        List<ChatChannelData> inData = channelFile.getAll();

        assertEquals(outData.size(), inData.size());

        for (int i = 0; i < outData.size(); i++) {
            final ChatChannelData outMessage = outData.get(i);
            final ChatChannelData inMessage = inData.get(i);
            assertEquals(outMessage, inMessage);
        }

    }

}
