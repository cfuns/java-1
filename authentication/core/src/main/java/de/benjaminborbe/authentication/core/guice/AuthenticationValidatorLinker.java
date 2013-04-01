package de.benjaminborbe.authentication.core.guice;

import com.google.inject.Inject;
import de.benjaminborbe.authentication.core.dao.UserValidator;
import de.benjaminborbe.tools.validation.ValidatorRegistry;

public class AuthenticationValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final UserValidator userValidator) {
		validatorRegistry.register(userValidator);
	}
}
