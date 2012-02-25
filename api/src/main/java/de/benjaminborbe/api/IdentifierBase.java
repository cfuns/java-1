package de.benjaminborbe.api;

public abstract class IdentifierBase<T> implements Identifier<T> {

	private final T id;

	public IdentifierBase(final T id) {
		this.id = id;
	}

	@Override
	public T getId() {
		return id;
	}

	@Override
	public String toString() {
		return String.valueOf(id);
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (id == null)
			return false;
		if (!(other instanceof Identifier))
			return false;
		if (!getClass().equals(other.getClass()))
			return false;
		return id.equals(((Identifier<?>) other).getId());
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : super.hashCode();
	}

}
