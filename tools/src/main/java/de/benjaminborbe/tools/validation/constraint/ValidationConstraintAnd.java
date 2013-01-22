package de.benjaminborbe.tools.validation.constraint;

public class ValidationConstraintAnd<T> implements ValidationConstraint<T> {

	private final ValidationConstraint<T>[] validationConstraints;

	public ValidationConstraintAnd(final ValidationConstraint<T>... validationConstraints) {
		this.validationConstraints = validationConstraints;
	}

	@Override
	public boolean validate(final T object) {
		for (final ValidationConstraint<T> validationConstraint : validationConstraints) {
			if (validationConstraint.precondition(object)) {
				if (!validationConstraint.validate(object)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean precondition(final T object) {
		return true;
	}

}
