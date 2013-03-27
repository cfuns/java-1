package de.benjaminborbe.tools.validation;

import java.util.Collection;

import de.benjaminborbe.api.ValidationError;

public interface ValidatorRule<T> {

	Collection<ValidationError> validate(final T object);
}
