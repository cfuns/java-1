package de.benjaminborbe.lib.validation.constraint;

public class ValidationConstraintIntegerLE implements ValidationConstraint<Integer> {

	private final int number;

	public ValidationConstraintIntegerLE(final int number) {
		this.number = number;
	}

	@Override
	public boolean validate(final Integer object) {
		return object <= number;
	}

	@Override
	public boolean precondition(final Integer object) {
		return object != null;
	}
}
