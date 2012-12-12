package de.benjaminborbe.tools.validation.constraint;

public class ValidationConstraintStringOnlyLetters extends ValidationConstraintCharacter {

	@Override
	protected boolean isAllowedCharacter(final char c) {
		return Character.isLetter(c);
	}

}
