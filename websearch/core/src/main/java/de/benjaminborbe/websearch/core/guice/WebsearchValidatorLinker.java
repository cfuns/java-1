package de.benjaminborbe.websearch.core.guice;

import de.benjaminborbe.lib.validation.ValidatorRegistry;
import de.benjaminborbe.websearch.core.dao.WebsearchConfigurationValidator;

import javax.inject.Inject;

public class WebsearchValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final WebsearchConfigurationValidator a) {
		validatorRegistry.register(a);
	}
}
