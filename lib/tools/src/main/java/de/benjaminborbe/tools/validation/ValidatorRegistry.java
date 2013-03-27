package de.benjaminborbe.tools.validation;

import java.util.Collection;

public interface ValidatorRegistry {

	Collection<Validator<?>> get(final Class<?> clazz);

	void register(Validator<?> validator);
}
