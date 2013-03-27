package de.benjaminborbe.tools.validation.constraint;

public class ValidationConstraintLongGT implements ValidationConstraint<Long> {

	private final int number;

	public ValidationConstraintLongGT(final int number) {
		this.number = number;
	}

	@Override
	public boolean validate(final Long object) {
		return object > number;
	}

	@Override
	public boolean precondition(final Long object) {
		return object != null;
	}
}
