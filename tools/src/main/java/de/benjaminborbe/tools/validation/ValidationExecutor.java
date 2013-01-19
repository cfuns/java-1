package de.benjaminborbe.tools.validation;

import java.util.Collection;

import de.benjaminborbe.api.ValidationResult;

public interface ValidationExecutor {

	ValidationResult validate(final Object object);

	ValidationResult validate(final Object object, Collection<String> fields);

}
