package de.benjaminborbe.lib.validation.constraint;

public class ValidationConstraintStringOnlyDigest extends ValidationConstraintStringCharacter {

	@Override
	protected boolean isAllowedCharacter(final char c) {
		return Character.isDigit(c);
	}

}
