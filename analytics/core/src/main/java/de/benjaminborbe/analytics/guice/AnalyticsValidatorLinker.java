package de.benjaminborbe.analytics.guice;

import de.benjaminborbe.analytics.dao.AnalyticsReportValidator;
import de.benjaminborbe.lib.validation.ValidatorRegistry;

import javax.inject.Inject;

public class AnalyticsValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final AnalyticsReportValidator analyticsReportValidator) {
		validatorRegistry.register(analyticsReportValidator);
	}
}
