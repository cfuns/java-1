package de.benjaminborbe.lib.validation.constraint;

public class ValidationConstraintStringNot implements ValidationConstraint<String> {

	private final String[] disallowedStrings;

	public ValidationConstraintStringNot(final String... disallowedStrings) {
		this.disallowedStrings = disallowedStrings;
	}

	@Override
	public boolean precondition(final String object) {
		return object != null;
	}

	@Override
	public boolean validate(final String object) {
		for (final String disallowedString : disallowedStrings) {
			if (disallowedString.equals(object)) {
				return false;
			}
		}
		return true;
	}

}
