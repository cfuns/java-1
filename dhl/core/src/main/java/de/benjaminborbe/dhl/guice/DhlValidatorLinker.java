package de.benjaminborbe.dhl.guice;

import com.google.inject.Inject;
import de.benjaminborbe.dhl.dao.DhlValidator;
import de.benjaminborbe.tools.validation.ValidatorRegistry;

public class DhlValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final DhlValidator dhlValidator) {
		validatorRegistry.register(dhlValidator);
	}
}
