package colab.server.file;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;

import colab.common.naming.CommunityName;
import colab.common.naming.UserName;
import colab.common.util.FileUtils;
import colab.server.Community;
import colab.server.CommunityCollection;
import colab.server.CommunityStore;
import colab.server.Password;

public class CommunityFile implements CommunityStore {

    private final CommunityCollection communities;

    private final File file;

    public CommunityFile(final File file) throws IOException {

        this.file = file;
        this.communities = new CommunityCollection();

        Scanner fileScanner = new Scanner(file);
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            if (line.indexOf(":") < 0) {
                continue;
            }
            Scanner lineScanner = new Scanner(line);
            lineScanner.useDelimiter(":");
            CommunityName name = new CommunityName(lineScanner.next());
            Password pass = new Password(lineScanner.next());
            Community community = new Community(name, pass);
            while (lineScanner.hasNext()) {
                UserName userName = new UserName(lineScanner.next());
                community.getMembers().add(userName);
            }
            communities.add(community);
            lineScanner.close();
        }
        fileScanner.close();

    }

    public Collection<Community> getAll() {
        return communities.getAll();
    }

    public void add(final Community community) {

        communities.add(community);

        try {
            String passHash = community.getPassword().getHash();
            String line = community.getId() + ":" + passHash + ":";
            for (UserName memberName : community.getMembers()) {
                line += memberName + ":";
            }
            FileUtils.appendLine(file, line);
        } catch (final IOException ioe) {
            ioe.printStackTrace();
        }

    }

    public Community get(final CommunityName name) {
        return communities.get(name);
    }

}
