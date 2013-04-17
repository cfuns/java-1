package de.benjaminborbe.weather;

import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.weather.guice.WeatherModules;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class WeatherActivator extends BaseBundleActivator {

	@Override
	protected Modules getModules(final BundleContext context) {
		return new WeatherModules(context);
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<>(super.getServiceTrackers(context));
		// serviceTrackers.add(new WeatherServiceTracker(weatherRegistry, context,
		// WeatherService.class));
		return serviceTrackers;
	}
}
