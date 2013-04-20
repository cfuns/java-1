package de.benjaminborbe.shortener.guice;

import javax.inject.Inject;

import de.benjaminborbe.shortener.dao.ShortenerUrlValidator;
import de.benjaminborbe.tools.validation.ValidatorRegistry;

public class ShortenerValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final ShortenerUrlValidator a) {
		validatorRegistry.register(a);
	}
}
