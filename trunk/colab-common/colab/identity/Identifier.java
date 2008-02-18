package colab.identity;

public abstract class Identifier<T> {

	protected final T value;

	public Identifier() {
		this.value = null;
	}

	public Identifier(final T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public int hashCode() {
		return value.hashCode();
	}

	public boolean equals(Object obj) {

		if (!(obj instanceof StringIdentifier)) {
			return false;
		}

		StringIdentifier other = (StringIdentifier) obj;
		return other.value == this.value;

	}

}
