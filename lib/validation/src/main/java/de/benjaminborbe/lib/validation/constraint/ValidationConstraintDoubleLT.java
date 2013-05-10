package de.benjaminborbe.lib.validation.constraint;

public class ValidationConstraintDoubleLT implements ValidationConstraint<Double> {

	private final double number;

	public ValidationConstraintDoubleLT(final double number) {
		this.number = number;
	}

	@Override
	public boolean validate(final Double object) {
		return object < number;
	}

	@Override
	public boolean precondition(final Double object) {
		return object != null;
	}
}
