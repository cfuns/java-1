package de.benjaminborbe.authentication.core.guice;

import de.benjaminborbe.authentication.core.dao.UserValidator;
import de.benjaminborbe.tools.validation.ValidatorRegistry;

import javax.inject.Inject;

public class AuthenticationValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final UserValidator userValidator) {
		validatorRegistry.register(userValidator);
	}
}
