package de.benjaminborbe.tools.validation.constraint;

public class ValidationConstraintLongLE implements ValidationConstraint<Long> {

	private final int number;

	public ValidationConstraintLongLE(final int number) {
		this.number = number;
	}

	@Override
	public boolean validate(final Long object) {
		return object <= number;
	}

	@Override
	public boolean precondition(final Long object) {
		return object != null;
	}
}
