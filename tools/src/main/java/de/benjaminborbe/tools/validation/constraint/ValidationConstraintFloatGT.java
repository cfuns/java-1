package de.benjaminborbe.tools.validation.constraint;

public class ValidationConstraintFloatGT implements ValidationConstraint<Float> {

	private final Float number;

	public ValidationConstraintFloatGT(final float number) {
		this.number = number;
	}

	@Override
	public boolean validate(final Float object) {
		return object > number;
	}

	@Override
	public boolean precondition(final Float object) {
		return object != null;
	}
}
