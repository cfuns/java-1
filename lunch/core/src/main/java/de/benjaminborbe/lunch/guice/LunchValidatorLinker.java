package de.benjaminborbe.lunch.guice;

import com.google.inject.Inject;

import de.benjaminborbe.lunch.dao.LunchUserSettingsValidator;
import de.benjaminborbe.tools.validation.ValidatorRegistry;

public class LunchValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final LunchUserSettingsValidator lunchUserSettingsValidator) {
		validatorRegistry.register(lunchUserSettingsValidator);
	}
}
