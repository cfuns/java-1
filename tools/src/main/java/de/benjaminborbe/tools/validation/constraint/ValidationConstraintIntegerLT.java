package de.benjaminborbe.tools.validation.constraint;

public class ValidationConstraintIntegerLT implements ValidationConstraint<Integer> {

	private final int number;

	public ValidationConstraintIntegerLT(final int number) {
		this.number = number;
	}

	@Override
	public boolean validate(final Integer object) {
		return object < number;
	}

	@Override
	public boolean precondition(final Integer object) {
		return object != null;
	}
}
