package colab.common.identity;

import junit.framework.TestCase;
import colab.common.user.User;
import colab.common.user.UserName;

public class IdentitySetTester extends TestCase {

    public void testUserSet() {

        IdentitySet<UserName, User> set = new IdentitySet<UserName, User>();

        User johannes = new User("Johannes", "pass1");
        set.add(johannes);

        User pamela = new User("Pamela", "pass2");
        set.add(pamela);

        User matt = new User("Matthew", "pass3");
        set.add(matt);

        User chris = new User("Chris", "pass4");
        set.add(chris);

        super.assertEquals(set.get(new UserName("Matthew")), matt);

    }

}
