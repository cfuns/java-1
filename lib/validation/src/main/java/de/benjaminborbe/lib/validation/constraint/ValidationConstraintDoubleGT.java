package de.benjaminborbe.lib.validation.constraint;

public class ValidationConstraintDoubleGT implements ValidationConstraint<Double> {

	private final Double number;

	public ValidationConstraintDoubleGT(final double number) {
		this.number = number;
	}

	@Override
	public boolean validate(final Double object) {
		return object > number;
	}

	@Override
	public boolean precondition(final Double object) {
		return object != null;
	}
}
