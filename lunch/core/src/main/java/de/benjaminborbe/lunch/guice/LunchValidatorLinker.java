package de.benjaminborbe.lunch.guice;

import javax.inject.Inject;

import de.benjaminborbe.lunch.dao.LunchUserSettingsValidator;
import de.benjaminborbe.tools.validation.ValidatorRegistry;

public class LunchValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final LunchUserSettingsValidator lunchUserSettingsValidator) {
		validatorRegistry.register(lunchUserSettingsValidator);
	}
}
