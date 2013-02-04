package de.benjaminborbe.message.guice;

import com.google.inject.Inject;

import de.benjaminborbe.message.dao.MessageValidator;
import de.benjaminborbe.tools.validation.ValidatorRegistry;

public class MessageValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final MessageValidator validator) {
		validatorRegistry.register(validator);
	}
}
