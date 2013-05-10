package de.benjaminborbe.lib.validation.constraint;

import java.util.ArrayList;
import java.util.List;

public class ValidationConstraintOr<T> implements ValidationConstraint<T> {

	private final List<ValidationConstraint<T>> validationConstraints = new ArrayList<>();

	public ValidationConstraintOr() {
	}

	public ValidationConstraintOr<T> add(final ValidationConstraint<T> validationConstraint) {
		validationConstraints.add(validationConstraint);
		return this;
	}

	@Override
	public boolean validate(final T object) {
		for (final ValidationConstraint<T> validationConstraint : validationConstraints) {
			if (validationConstraint.precondition(object)) {
				if (validationConstraint.validate(object)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean precondition(final T object) {
		return true;
	}

}
