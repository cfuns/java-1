package de.benjaminborbe.tools.validation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationResult;

public class ValidationResultImpl implements ValidationResult {

	private final Set<ValidationError> errors = new HashSet<ValidationError>();

	public ValidationResultImpl() {
	}

	public ValidationResultImpl(final ValidationError... validationErrors) {
		for (final ValidationError validationError : validationErrors) {
			add(validationError);
		}
	}

	@Override
	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	@Override
	public String toString() {
		return "ValidationResult has " + errors.size() + " errors";
	}

	@Override
	public Collection<ValidationError> getErrors() {
		return errors;
	}

	public void add(final ValidationError validationError) {
		errors.add(validationError);
	}

	public void addAll(final Collection<? extends ValidationError> validationErrors) {
		errors.addAll(validationErrors);
	}

}
