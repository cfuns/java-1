package de.benjaminborbe.tools.validation.constraint;

public class ValidationConstraintMinLength implements ValidationConstraint<String> {

	private final int minLength;

	public ValidationConstraintMinLength(final int minLength) {
		this.minLength = minLength;
	}

	@Override
	public boolean validate(final String object) {
		return object != null && object.length() >= minLength;
	}

}
