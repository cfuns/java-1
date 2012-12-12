package de.benjaminborbe.tools.validation.constraint;

public class ValidationConstraintMaxLength implements ValidationConstraint<String> {

	private final int maxLength;

	public ValidationConstraintMaxLength(final int maxLength) {
		this.maxLength = maxLength;
	}

	@Override
	public boolean validate(final String object) {
		return object != null && object.length() <= maxLength;
	}

}
