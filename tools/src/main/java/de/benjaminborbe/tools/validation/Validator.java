package de.benjaminborbe.tools.validation;

import java.util.Collection;

public interface Validator<T> {

	Class<T> getType();

	Collection<ValidationError> validate(Object object);
}
