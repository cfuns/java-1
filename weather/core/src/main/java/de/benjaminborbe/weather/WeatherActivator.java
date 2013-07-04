package de.benjaminborbe.weather;

import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.weather.guice.WeatherModules;
import org.osgi.framework.BundleContext;

public class WeatherActivator extends BaseBundleActivator {

	@Override
	protected Modules getModules(final BundleContext context) {
		return new WeatherModules(context);
	}

}
