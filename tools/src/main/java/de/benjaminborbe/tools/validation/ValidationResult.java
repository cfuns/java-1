package de.benjaminborbe.tools.validation;

import java.util.HashSet;

public class ValidationResult extends HashSet<ValidationError> {

	private static final long serialVersionUID = -2748154180929748549L;

	public boolean hasErrors() {
		return !isEmpty();
	}

	@Override
	public String toString() {
		return "ValidationResult has " + size() + " errors";
	}

}
