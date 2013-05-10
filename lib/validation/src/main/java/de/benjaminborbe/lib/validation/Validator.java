package de.benjaminborbe.lib.validation;

import de.benjaminborbe.api.ValidationError;

import java.util.Collection;

public interface Validator<T> {

	Class<T> getType();

	Collection<ValidationError> validateObject(Object object);

	Collection<ValidationError> validate(T object);

	Collection<ValidationError> validateObject(Object object, Collection<String> fields);

	Collection<ValidationError> validate(T object, Collection<String> fields);

}
