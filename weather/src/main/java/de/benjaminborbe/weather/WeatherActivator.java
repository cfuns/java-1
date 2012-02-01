package de.benjaminborbe.weather;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.weather.guice.WeatherModules;

public class WeatherActivator extends BaseBundleActivator {

	@Override
	protected Modules getModules(final BundleContext context) {
		return new WeatherModules(context);
	}

	@Override
	protected Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new WeatherServiceTracker(weatherRegistry, context,
		// WeatherService.class));
		return serviceTrackers;
	}
}
