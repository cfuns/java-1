package de.benjaminborbe.api;

import java.util.Collection;

public interface ValidationResult {

	boolean hasErrors();

	Collection<ValidationError> getErrors();

}
