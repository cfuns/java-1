package de.benjaminborbe.tools.validation;

import java.util.Collection;

import de.benjaminborbe.api.ValidationError;

public interface Validator<T> {

	Class<T> getType();

	Collection<ValidationError> validate(Object object);
}
