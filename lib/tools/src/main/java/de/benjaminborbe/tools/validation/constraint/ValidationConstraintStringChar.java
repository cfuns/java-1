package de.benjaminborbe.tools.validation.constraint;

public class ValidationConstraintStringChar extends ValidationConstraintStringCharacter {

	private final char[] chars;

	public ValidationConstraintStringChar(final char... chars) {
		this.chars = chars;
	}

	@Override
	protected boolean isAllowedCharacter(final char character) {
		for (final char c : chars) {
			if (character == c) {
				return true;
			}
		}
		return false;
	}

}
