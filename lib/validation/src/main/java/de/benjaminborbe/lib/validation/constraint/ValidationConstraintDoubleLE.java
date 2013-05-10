package de.benjaminborbe.lib.validation.constraint;

public class ValidationConstraintDoubleLE implements ValidationConstraint<Double> {

	private final double number;

	public ValidationConstraintDoubleLE(final double number) {
		this.number = number;
	}

	@Override
	public boolean validate(final Double object) {
		return object <= number;
	}

	@Override
	public boolean precondition(final Double object) {
		return object != null;
	}
}
