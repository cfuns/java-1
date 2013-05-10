package de.benjaminborbe.monitoring.guice;

import de.benjaminborbe.lib.validation.ValidatorRegistry;
import de.benjaminborbe.monitoring.dao.MonitoringNodeValidator;

import javax.inject.Inject;

public class MonitoringValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final MonitoringNodeValidator monitoringNodeValidator) {
		validatorRegistry.register(monitoringNodeValidator);
	}
}
