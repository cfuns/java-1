package de.benjaminborbe.monitoring.guice;

import com.google.inject.Inject;

import de.benjaminborbe.monitoring.dao.MonitoringNodeValidator;
import de.benjaminborbe.tools.validation.ValidatorRegistry;

public class MonitoringValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final MonitoringNodeValidator monitoringNodeValidator) {
		validatorRegistry.register(monitoringNodeValidator);
	}
}
