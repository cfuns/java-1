package de.benjaminborbe.api;

import java.util.Collection;

public class ValidationException extends Exception implements ValidationResult {

	private static final long serialVersionUID = -5593791613878769493L;

	private final ValidationResult validationResult;

	public ValidationException(final ValidationResult validationResult) {
		this.validationResult = validationResult;
	}

	@Override
	public boolean hasErrors() {
		return validationResult.hasErrors();
	}

	@Override
	public Collection<ValidationError> getErrors() {
		return validationResult.getErrors();
	}

	@Override
	public String toString() {
		return super.toString() + " " + validationResult;
	}

}
