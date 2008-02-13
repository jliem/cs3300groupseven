package colab.common;

/**
 * Represents a User
 * 
 * @author JL
 *
 */
public class User {

	protected String name;
	
	public User() {
		this(null);
	}
	
	public User(String name) {
		
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
