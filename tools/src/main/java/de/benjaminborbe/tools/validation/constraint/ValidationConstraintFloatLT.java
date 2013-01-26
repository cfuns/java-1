package de.benjaminborbe.tools.validation.constraint;

public class ValidationConstraintFloatLT implements ValidationConstraint<Float> {

	private final float number;

	public ValidationConstraintFloatLT(final float number) {
		this.number = number;
	}

	@Override
	public boolean validate(final Float object) {
		return object < number;
	}

	@Override
	public boolean precondition(final Float object) {
		return object != null;
	}
}
