package de.benjaminborbe.lib.validation.constraint;

public class ValidationConstraintStringCharLowerLetter extends ValidationConstraintStringCharacter {

	@Override
	protected boolean isAllowedCharacter(final char character) {
		return Character.isLowerCase(character);
	}
}
