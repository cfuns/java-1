package de.benjaminborbe.confluence.validation;

import javax.inject.Inject;
import de.benjaminborbe.tools.validation.ValidatorRegistry;

public class ConfluenceInstanceValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final ConfluenceInstanceValidator validator) {
		validatorRegistry.register(validator);
	}
}
