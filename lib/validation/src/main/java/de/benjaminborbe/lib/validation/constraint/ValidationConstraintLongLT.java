package de.benjaminborbe.lib.validation.constraint;

public class ValidationConstraintLongLT implements ValidationConstraint<Long> {

	private final int number;

	public ValidationConstraintLongLT(final int number) {
		this.number = number;
	}

	@Override
	public boolean validate(final Long object) {
		return object < number;
	}

	@Override
	public boolean precondition(final Long object) {
		return object != null;
	}
}
