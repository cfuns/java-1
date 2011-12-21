package de.benjaminborbe.dashboard.service;

import org.osgi.framework.BundleContext;
import com.google.inject.Singleton;

import de.benjaminborbe.dashboard.api.DashboardWidget;

@Singleton
public class DashboardWidgetServiceTracker extends RegistryServiceTracker<DashboardWidget> {

	public DashboardWidgetServiceTracker(
			final DashboardWidgetRegistry registry,
			final BundleContext context,
			final Class<?> clazz) {
		super(registry, context, clazz);
	}

}
