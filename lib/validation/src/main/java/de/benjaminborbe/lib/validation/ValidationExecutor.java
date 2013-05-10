package de.benjaminborbe.lib.validation;

import de.benjaminborbe.api.ValidationResult;

import java.util.Collection;

public interface ValidationExecutor {

	ValidationResult validate(final Object object);

	ValidationResult validate(final Object object, Collection<String> fields);

}
