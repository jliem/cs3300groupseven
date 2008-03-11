package colab.server.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import junit.framework.TestCase;
import colab.common.naming.UserName;
import colab.server.file.UserFile;
import colab.server.user.User;

public class UserFileTester extends TestCase {

    private String fileContents;

    private String createTestContents() throws Exception {

        User matthew = new User("Matthew", "pass3".toCharArray());
        User chris = new User("Chris", "pass4".toCharArray());

        return matthew.getId() + ":"
             + new String(matthew.getPassword().getHash()) + "\n"
             + chris.getId()   + ":"
             + new String(chris.getPassword().getHash()) + "\n";

    }

    public void testReadFile() throws Exception {

        File file = File.createTempFile("colabTestReadFile", null);
        PrintWriter writer =
            new PrintWriter(new BufferedWriter(new FileWriter(file)));
        try {
            writer.write(createTestContents());
        } finally {
            writer.close();
        }

        UserFile userFile = new UserFile(file);

        User doesNotExist = userFile.get(new UserName("DoesNotExist"));
        assertNull(doesNotExist);

        User matthew = userFile.get(new UserName("Matthew"));
        assertNotNull(matthew);
        assertTrue(matthew.checkPassword("pass3".toCharArray()));

        User chris = userFile.get(new UserName("Chris"));
        assertNotNull(chris);
        assertTrue(chris.checkPassword("pass4".toCharArray()));


    }

}
