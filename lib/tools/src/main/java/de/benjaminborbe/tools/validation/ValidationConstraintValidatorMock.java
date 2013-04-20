package de.benjaminborbe.tools.validation;

import javax.inject.Inject;
import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ValidationConstraintValidatorMock extends ValidationConstraintValidator {

	@Inject
	public ValidationConstraintValidatorMock() {
	}

	@Override
	public <T> Collection<ValidationError> validate(final String name, final T value, final Collection<ValidationConstraint<T>> constraints) {
		final List<ValidationError> result = new ArrayList<>();
		return result;
	}
}
