package de.benjaminborbe.lib.validation.constraint;

public class ValidationConstraintStringMinLength implements ValidationConstraint<String> {

	private final int minLength;

	public ValidationConstraintStringMinLength(final int minLength) {
		this.minLength = minLength;
	}

	@Override
	public boolean validate(final String object) {
		return object.length() >= minLength;
	}

	@Override
	public boolean precondition(final String object) {
		return object != null;
	}

}
