package de.benjaminborbe.selenium.configuration.simple;

import de.benjaminborbe.selenium.configuration.simple.guice.SeleniumConfigurationSimpleModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import org.osgi.framework.BundleContext;

public class SeleniumConfigurationSimpleActivator extends BaseBundleActivator {

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SeleniumConfigurationSimpleModules(context);
	}
}
