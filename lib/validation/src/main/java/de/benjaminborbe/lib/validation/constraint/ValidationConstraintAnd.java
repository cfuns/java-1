package de.benjaminborbe.lib.validation.constraint;

import java.util.ArrayList;
import java.util.List;

public class ValidationConstraintAnd<T> implements ValidationConstraint<T> {

	private final List<ValidationConstraint<T>> validationConstraints = new ArrayList<>();

	public ValidationConstraintAnd() {
	}

	public ValidationConstraintAnd<T> add(final ValidationConstraint<T> validationConstraint) {
		validationConstraints.add(validationConstraint);
		return this;
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
