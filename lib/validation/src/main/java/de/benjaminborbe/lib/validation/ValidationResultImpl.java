package de.benjaminborbe.lib.validation;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationResult;

import java.io.StringWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ValidationResultImpl implements ValidationResult {

	private final Set<ValidationError> errors = new HashSet<>();

	public ValidationResultImpl() {
	}

	public ValidationResultImpl(final ValidationError... validationErrors) {
		for (final ValidationError validationError : validationErrors) {
			add(validationError);
		}
	}

	public ValidationResultImpl(final Collection<ValidationError> validationErrors) {
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
		final StringWriter sw = new StringWriter();
		sw.append("ValidationResult has ");
		sw.append(String.valueOf(errors.size()));
		sw.append(" errors! [");
		boolean first = true;
		for (final ValidationError error : errors) {
			if (first) {
				first = false;
			} else {
				sw.append(", ");
			}
			sw.append(String.valueOf(error));
		}
		sw.append("]");
		return sw.toString();
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
