package de.benjaminborbe.lib.validation;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraint;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ValidationConstraintValidatorMock extends ValidationConstraintValidator {

	@Inject
	public ValidationConstraintValidatorMock() {
	}

	@Override
	public <T> Collection<ValidationError> validate(final String name, final T value, final Collection<ValidationConstraint<T>> constraints) {
		final List<ValidationError> result = new ArrayList<ValidationError>();
		return result;
	}
}
