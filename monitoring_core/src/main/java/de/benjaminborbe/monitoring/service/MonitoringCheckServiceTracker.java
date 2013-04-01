package de.benjaminborbe.monitoring.service;

import com.google.inject.Singleton;
import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.check.MonitoringCheckRegistry;
import de.benjaminborbe.tools.osgi.service.RegistryServiceTracker;
import org.osgi.framework.BundleContext;

@Singleton
public class MonitoringCheckServiceTracker extends RegistryServiceTracker<MonitoringCheck> {

	public MonitoringCheckServiceTracker(final MonitoringCheckRegistry registry, final BundleContext context, final Class<?> clazz) {
		super(registry, context, clazz);
	}

}
