package colab.community;

import colab.identifier.Identifier;

public abstract class StringIdentifier implements Identifier<String> {

	private final String value;

	public StringIdentifier() {
		this.value = null;
	}
	
	public StringIdentifier(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}
