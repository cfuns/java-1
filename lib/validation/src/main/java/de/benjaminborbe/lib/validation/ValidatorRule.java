package de.benjaminborbe.lib.validation;

import de.benjaminborbe.api.ValidationError;

import java.util.Collection;

public interface ValidatorRule<T> {

	Collection<ValidationError> validate(final T object);
}
