package de.benjaminborbe.api;

public class ValidationErrorSimple implements ValidationError {

	private final String message;

	public ValidationErrorSimple(final String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return message;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (message == null)
			return false;
		if (!(other instanceof ValidationErrorSimple))
			return false;
		if (!getClass().equals(other.getClass()))
			return false;
		return message.equals(((ValidationErrorSimple) other).message);
	}

	@Override
	public int hashCode() {
		return message.hashCode();
	}

}
