package de.benjaminborbe.websearch.core.guice;

import javax.inject.Inject;
import de.benjaminborbe.tools.validation.ValidatorRegistry;
import de.benjaminborbe.websearch.core.dao.WebsearchConfigurationValidator;

public class WebsearchValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final WebsearchConfigurationValidator a) {
		validatorRegistry.register(a);
	}
}
