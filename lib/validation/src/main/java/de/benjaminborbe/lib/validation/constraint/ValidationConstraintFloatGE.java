package de.benjaminborbe.lib.validation.constraint;

public class ValidationConstraintFloatGE implements ValidationConstraint<Float> {

	private final float number;

	public ValidationConstraintFloatGE(final float number) {
		this.number = number;
	}

	@Override
	public boolean validate(final Float object) {
		return object >= number;
	}

	@Override
	public boolean precondition(final Float object) {
		return object != null;
	}
}
