package de.benjaminborbe.authentication.core.util;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.tools.password.PasswordCharacter;
import de.benjaminborbe.tools.password.PasswordGenerator;
import de.benjaminborbe.tools.password.PasswordValidator;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class AuthenticationPasswordUtil {

	private final PasswordValidator passwordValidator;

	private final PasswordGenerator passwordGenerator;

	@Inject
	public AuthenticationPasswordUtil(final PasswordValidator passwordValidator, final PasswordGenerator passwordGenerator) {
		this.passwordValidator = passwordValidator;
		this.passwordGenerator = passwordGenerator;
	}

	public String generatePassword() throws AuthenticationGeneratePasswordFailedException {
		for (int i = 0; i < 100; ++i) {
			final String password = passwordGenerator.generatePassword(16, PasswordCharacter.values());
			if (validatePassword(password).isEmpty()) {
				return password;
			}
		}
		throw new AuthenticationGeneratePasswordFailedException("generate password failed");
	}

	public List<ValidationError> validatePassword(final String password) {
		final List<ValidationError> errors = new ArrayList<ValidationError>();
		if (!passwordValidator.hasDigest(password, 1)) {
			errors.add(new ValidationErrorSimple("password contains no digest characters"));
		}
		if (!passwordValidator.hasLength(password, 8)) {
			errors.add(new ValidationErrorSimple("password at least 8 characters"));
		}
		if (!passwordValidator.hasValidChars(password)) {
			errors.add(new ValidationErrorSimple("password contains invalid characters"));
		}
		if (!passwordValidator.hasLowerCharacter(password, 1)) {
			errors.add(new ValidationErrorSimple("password at least one lower letter"));
		}
		if (!passwordValidator.hasSpecialCharacter(password, 1)) {
			errors.add(new ValidationErrorSimple("password at least one special character"));
		}
		if (!passwordValidator.hasUpperCharacter(password, 1)) {
			errors.add(new ValidationErrorSimple("password at least one upper letter"));
		}
		return errors;
	}

}
