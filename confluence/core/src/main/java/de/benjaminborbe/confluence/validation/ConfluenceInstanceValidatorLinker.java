package de.benjaminborbe.confluence.validation;

import de.benjaminborbe.lib.validation.ValidatorRegistry;

import javax.inject.Inject;

public class ConfluenceInstanceValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final ConfluenceInstanceValidator validator) {
		validatorRegistry.register(validator);
	}
}
