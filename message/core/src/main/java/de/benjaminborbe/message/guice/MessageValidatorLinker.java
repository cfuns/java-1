package de.benjaminborbe.message.guice;

import de.benjaminborbe.lib.validation.ValidatorRegistry;
import de.benjaminborbe.message.dao.MessageValidator;

import javax.inject.Inject;

public class MessageValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final MessageValidator validator) {
		validatorRegistry.register(validator);
	}
}
