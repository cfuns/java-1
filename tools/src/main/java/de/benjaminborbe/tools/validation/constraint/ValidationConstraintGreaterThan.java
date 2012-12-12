package de.benjaminborbe.tools.validation.constraint;

public class ValidationConstraintGreaterThan implements ValidationConstraint<Integer> {

	private final int number;

	public ValidationConstraintGreaterThan(final int number) {
		this.number = number;
	}

	@Override
	public boolean validate(final Integer object) {
		return object != null && object > number;
	}
}
