package colab.identity;


public abstract class StringIdentifier extends Identifier<String> {

	public StringIdentifier() {
		super();
	}
	
	public StringIdentifier(final String str) {
		super(str);
	}
	
	@Override
	public String toString() {
		return value;
	}
	
}
