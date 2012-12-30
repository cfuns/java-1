package de.benjaminborbe.websearch.guice;

import com.google.inject.Inject;

import de.benjaminborbe.tools.validation.ValidatorRegistry;
import de.benjaminborbe.websearch.dao.WebsearchConfigurationValidator;

public class WebsearchValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final WebsearchConfigurationValidator a) {
		validatorRegistry.register(a);
	}
}
