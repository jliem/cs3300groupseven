package colab.identity;

import java.io.Serializable;

public abstract class Identifier<T> implements Serializable {

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

	@Override
	public int hashCode() {
		return (value == null) ? 0 : value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof Identifier)) {
			return false;
		}

		Identifier other = (Identifier) obj;
		return other.value.equals(this.value);

	}

}
