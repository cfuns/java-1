package de.benjaminborbe.lunch.guice;

import de.benjaminborbe.lib.validation.ValidatorRegistry;
import de.benjaminborbe.lunch.dao.LunchUserSettingsValidator;

import javax.inject.Inject;

public class LunchValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final LunchUserSettingsValidator lunchUserSettingsValidator) {
		validatorRegistry.register(lunchUserSettingsValidator);
	}
}
