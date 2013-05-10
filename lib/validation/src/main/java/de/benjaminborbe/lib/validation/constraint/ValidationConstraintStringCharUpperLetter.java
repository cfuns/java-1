package de.benjaminborbe.lib.validation.constraint;

public class ValidationConstraintStringCharUpperLetter extends ValidationConstraintStringCharacter {

	@Override
	protected boolean isAllowedCharacter(final char character) {
		return Character.isUpperCase(character);
	}
}
