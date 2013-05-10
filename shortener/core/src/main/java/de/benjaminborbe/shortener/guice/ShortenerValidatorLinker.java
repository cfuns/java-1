package de.benjaminborbe.shortener.guice;

import de.benjaminborbe.lib.validation.ValidatorRegistry;
import de.benjaminborbe.shortener.dao.ShortenerUrlValidator;

import javax.inject.Inject;

public class ShortenerValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final ShortenerUrlValidator a) {
		validatorRegistry.register(a);
	}
}
