package de.benjaminborbe.monitoring.service;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;

import com.google.inject.Singleton;

import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.check.MonitoringCheckRegistry;
import de.benjaminborbe.tools.osgi.service.RegistryServiceTracker;

@Singleton
public class MonitoringCheckServiceTracker extends RegistryServiceTracker<MonitoringCheck> {

	public MonitoringCheckServiceTracker(final Logger logger, final MonitoringCheckRegistry registry, final BundleContext context, final Class<?> clazz) {
		super(registry, context, clazz);
	}

}
