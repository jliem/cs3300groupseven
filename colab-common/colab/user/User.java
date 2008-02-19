package colab.user;

import java.io.Serializable;

import colab.identity.Identifiable;

/**
 * Represents a User.
 */
public class User implements Identifiable<UserName>, Serializable {

	public static final long serialVersionUID = 1L;
	
	private final UserName name;
	private Password pass;

	public User(final UserName name, final Password pass) {
		this.name = name;
		this.pass = pass;
	}
	
	public User(final String name, final String pass) {
		this(new UserName(name), new Password(pass));
	}

	public void setPassword(final Password pass) {
		this.pass = pass;
	}

	public boolean checkPassword(final String attempt) {
		return pass.checkPassword(attempt);
	}
	
	/**
	 * @return the name of this user
	 */
	public UserName getId() {
		return name;
	}
	
}
