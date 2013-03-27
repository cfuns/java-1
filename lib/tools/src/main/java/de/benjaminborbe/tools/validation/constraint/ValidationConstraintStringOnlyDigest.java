package de.benjaminborbe.tools.validation.constraint;

public class ValidationConstraintStringOnlyDigest extends ValidationConstraintStringCharacter {

	@Override
	protected boolean isAllowedCharacter(final char c) {
		return Character.isDigit(c);
	}

}
