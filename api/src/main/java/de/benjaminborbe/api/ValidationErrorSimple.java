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

}
