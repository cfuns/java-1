package de.benjaminborbe.tools.validation.constraint;

public abstract class ValidationConstraintStringCharacter implements ValidationConstraint<String> {

	@Override
	public boolean validate(final String object) {
		if (object == null)
			return false;
		for (final char c : object.toCharArray()) {
			if (!isAllowedCharacter(c)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean precondition(final String object) {
		return object != null;
	}

	protected abstract boolean isAllowedCharacter(final char character);
}
