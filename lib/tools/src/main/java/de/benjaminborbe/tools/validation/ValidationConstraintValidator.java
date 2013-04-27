package de.benjaminborbe.tools.validation;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ValidationConstraintValidator {

	@Inject
	public ValidationConstraintValidator() {
	}

	public <T> Collection<ValidationError> validate(final String name, final T value, final Collection<ValidationConstraint<T>> constraints) {
		final List<ValidationError> result = new ArrayList<>();
		for (final ValidationConstraint<T> constraint : constraints) {
			if (constraint.precondition(value) && !constraint.validate(value)) {
				result.add(new ValidationErrorSimple(name + " is invalid"));
			}
		}
		return result;
	}
}
