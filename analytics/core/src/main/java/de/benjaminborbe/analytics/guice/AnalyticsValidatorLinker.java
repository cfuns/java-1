package de.benjaminborbe.analytics.guice;

import javax.inject.Inject;

import de.benjaminborbe.analytics.dao.AnalyticsReportValidator;
import de.benjaminborbe.tools.validation.ValidatorRegistry;

public class AnalyticsValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final AnalyticsReportValidator analyticsReportValidator) {
		validatorRegistry.register(analyticsReportValidator);
	}
}
