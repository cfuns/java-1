package de.benjaminborbe.tools.validation.constraint;

public class ValidationConstraintLongGE implements ValidationConstraint<Long> {

	private final int number;

	public ValidationConstraintLongGE(final int number) {
		this.number = number;
	}

	@Override
	public boolean validate(final Long object) {
		return object >= number;
	}

	@Override
	public boolean precondition(final Long object) {
		return object != null;
	}
}
