package de.benjaminborbe.dhl.guice;

import de.benjaminborbe.dhl.dao.DhlValidator;
import de.benjaminborbe.tools.validation.ValidatorRegistry;

import javax.inject.Inject;

public class DhlValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final DhlValidator dhlValidator) {
		validatorRegistry.register(dhlValidator);
	}
}
