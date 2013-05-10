package de.benjaminborbe.lib.validation.constraint;

public class ValidationConstraintStringMaxLength implements ValidationConstraint<String> {

	private final int maxLength;

	public ValidationConstraintStringMaxLength(final int maxLength) {
		this.maxLength = maxLength;
	}

	@Override
	public boolean validate(final String object) {
		return object.length() <= maxLength;
	}

	@Override
	public boolean precondition(final String object) {
		return object != null;
	}

}
