package colab.server.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import colab.server.user.User;

public final class UserFileTester extends TestCase {

    public void testWriteAndRead() throws Exception {

        File file = File.createTempFile("colabTestReadFile", null);
        UserFile userFile = new UserFile(file);

        List<User> outData = new ArrayList<User>();
        outData.add(new User("Matthew", "pass3".toCharArray()));
        outData.add(new User("Chris", "pass4".toCharArray()));

        for (User user : outData) {
            userFile.add(user);
        }

        userFile = new UserFile(file);

        for (User expectedUser : outData) {
            User actualUser = userFile.get(expectedUser.getId());
            assertNotNull(actualUser);
            assertEquals(expectedUser, actualUser);
        }

    }

}
