package de.benjaminborbe.authentication.guice;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.dao.UserValidator;
import de.benjaminborbe.tools.validation.ValidatorRegistry;

public class AuthenticationValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final UserValidator userValidator) {
		validatorRegistry.register(userValidator);
	}
}
