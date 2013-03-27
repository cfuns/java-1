package de.benjaminborbe.tools.password;

import com.google.inject.Inject;

public class PasswordValidatorImpl implements PasswordValidator {

	@Inject
	public PasswordValidatorImpl() {
	}

	@Override
	public boolean hasLength(final String password, final int minLength) {
		if (password == null) {
			return false;
		}
		return password.length() >= minLength;
	}

	@Override
	public boolean hasValidChars(final String password) {
		if (password == null) {
			return false;
		}
		for (final char c : password.toCharArray()) {
			boolean notFound = true;
			for (final PasswordCharacter pc : PasswordCharacter.values()) {
				if (pc.containsChar(c)) {
					notFound = false;
				}
			}
			if (notFound) {
				return false;
			}
		}
		return true;
	}

	private boolean has(final String password, final int minAmount, final PasswordCharacter passwordCharacter) {
		if (password == null) {
			return false;
		}

		int match = 0;
		for (final char c : password.toCharArray()) {
			if (passwordCharacter.containsChar(c)) {
				match++;
			}
		}
		return match >= minAmount;
	}

	@Override
	public boolean hasDigest(final String password, final int amount) {
		return has(password, amount, PasswordCharacter.NUMBER);
	}

	@Override
	public boolean hasLowerCharacter(final String password, final int amount) {
		return has(password, amount, PasswordCharacter.LOWER);
	}

	@Override
	public boolean hasUpperCharacter(final String password, final int amount) {
		return has(password, amount, PasswordCharacter.UPPER);
	}

	@Override
	public boolean hasSpecialCharacter(final String password, final int amount) {
		return has(password, amount, PasswordCharacter.SPECIAL);
	}
}
