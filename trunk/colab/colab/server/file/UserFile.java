package colab.server.file;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import colab.common.naming.UserName;
import colab.common.util.FileUtils;
import colab.server.user.Password;
import colab.server.user.User;
import colab.server.user.UserCollection;
import colab.server.user.UserStore;

public final class UserFile implements UserStore {

    private final UserCollection users;

    private final File file;

    public UserFile(final File file) throws IOException {

        this.file = file;
        this.users = new UserCollection();

        Scanner fileScanner = new Scanner(file);
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            if (line.indexOf(":") < 0) {
                continue;
            }
            Scanner lineScanner = new Scanner(line);
            lineScanner.useDelimiter(":");
            UserName name = new UserName(lineScanner.next());
            Password pass = new Password(lineScanner.next());
            users.add(new User(name, pass));
        }
        fileScanner.close();

    }

    /** {@inheritDoc} */
    public void add(final User user) {

        System.out.println("Adding to file: " + user.getId());

        users.add(user);

        try {
            String passHash = user.getPassword().getHash();
            String line = user.getId() + ":" + passHash;
            FileUtils.appendLine(file, line);
            System.out.println("Appended: " + line);
        } catch (final IOException ioe) {
            ioe.printStackTrace();
        }

    }

    /** {@inheritDoc} */
    public User get(final UserName name) {
        return users.get(name);
    }

}
