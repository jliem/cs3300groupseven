package colab.user;

import junit.framework.TestCase;

public class UserTester extends TestCase {

	public void testStringConstructor() {
		
		User u = new User("Johannes", "password");
		assertEquals(u.getId().getValue(), "Johannes");
		
	}
	
}
