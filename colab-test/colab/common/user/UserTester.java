package colab.common.user;

import colab.common.user.User;
import junit.framework.TestCase;

public class UserTester extends TestCase {

    public void testStringConstructor() {
        
        User u = new User("Johannes", "password");
        assertEquals(u.getId().getValue(), "Johannes");
        
    }
    
}
