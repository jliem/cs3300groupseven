package colab.server;

import colab.common.DebugManager;
import colab.common.Logger;
import colab.common.exception.CommunityDoesNotExistException;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;
import colab.server.user.Community;
import colab.server.user.User;
import colab.server.user.UserManager;

/**
 * The Mock Colab Server is a server implementation which comes
 * with some pre-loaded data and does not save anything to the
 * filesystem.  It is intended only for demonstration and testing.
 */
public final class MockColabServer extends ColabServer {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * Constructs a new MockColabServer, populated with some
     * basic user and community data.
     *
     * @throws Exception if any exception is thrown
     */
    public MockColabServer() throws Exception {

        // Populate a few test users

        User johannes = new User("Johannes", "pass1".toCharArray());
        addUser(johannes);

        User pamela = new User("Pamela", "pass2".toCharArray());
        addUser(pamela);

        User matthew = new User("Matthew", "pass3".toCharArray());
        addUser(matthew);

        User chris = new User("Chris", "pass4".toCharArray());
        addUser(chris);

        // Populate a few test communities

        Community groupSeven = new Community("Group Seven", "sevenPass");
        createCommunity(groupSeven.getId(), groupSeven.getPassword());
        addAsMember(johannes.getId(), groupSeven.getId());
        addAsMember(pamela.getId(), groupSeven.getId());
        addAsMember(matthew.getId(), groupSeven.getId());
        addAsMember(chris.getId(), groupSeven.getId());

        Community teamAwesome = new Community("Team Awesome", "awesomePass");
        createCommunity(teamAwesome.getId(), teamAwesome.getPassword());
        addAsMember(chris.getId(), teamAwesome.getId());

        Community noMembers = new Community("The No-Members Community", "abcd");
        createCommunity(noMembers.getId(), noMembers.getPassword());

    }

    /**
     * Adds a user as a member of a community.
     *
     * @param userName the user
     * @param communityName the name of the community
     * @throws CommunityDoesNotExistException if no such community exists
     */
    public void addAsMember(final UserName userName,
            final CommunityName communityName)
            throws CommunityDoesNotExistException {

        // Look up the community
        UserManager userManager = super.getUserManager();
        Community comm = userManager.getCommunity(communityName);
        if (comm != null) {
            comm.addMember(userName);
        }

    }

    /**
     * Entry point which launches a mock colab server application.
     *
     * @param args one argument is accepted:
     *             [optional] an integer specifying which port to run on
     * @throws Exception if any exception is thrown
     */
    public static void main(final String[] args) throws Exception {

        DebugManager.enableExceptions(true);

        Logger.enable(true);

        // Default port
        Integer port = 9040;

        // Get arguments
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        // Create and initialize a server
        new MockColabServer().publish(port);

    }

}
