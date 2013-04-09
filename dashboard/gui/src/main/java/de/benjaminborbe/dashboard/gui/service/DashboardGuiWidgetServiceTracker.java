package de.benjaminborbe.dashboard.gui.service;

import org.osgi.framework.BundleContext;

import com.google.inject.Singleton;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.tools.osgi.service.RegistryServiceTracker;

@Singleton
public class DashboardGuiWidgetServiceTracker extends RegistryServiceTracker<DashboardContentWidget> {

	public DashboardGuiWidgetServiceTracker(final DashboardGuiWidgetRegistry registry, final BundleContext context, final Class<?> clazz) {
		super(registry, context, clazz);
	}

}
