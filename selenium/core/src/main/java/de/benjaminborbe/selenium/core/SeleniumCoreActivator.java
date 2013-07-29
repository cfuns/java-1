package de.benjaminborbe.selenium.core;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.selenium.api.SeleniumService;
import de.benjaminborbe.selenium.core.config.SeleniumCoreConfig;
import de.benjaminborbe.selenium.core.configuration.SeleniumConfigurationRegistry;
import de.benjaminborbe.selenium.core.guice.SeleniumCoreModules;
import de.benjaminborbe.selenium.core.service.SeleniumConfigurationServiceTracker;
import de.benjaminborbe.selenium.core.util.SeleniumCoreWebDriver;
import de.benjaminborbe.selenium.core.util.SeleniumCoreWebDriverRegistry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SeleniumCoreActivator extends BaseBundleActivator {

	@Inject
	private SeleniumConfigurationRegistry seleniumConfigurationRegistry;

	@Inject
	private SeleniumCoreConfig seleniumCoreConfig;

	@Inject
	private SeleniumService seleniumService;

	@Inject
	private SeleniumCoreWebDriverRegistry seleniumCoreWebDriverRegistry;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SeleniumCoreModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(SeleniumService.class, seleniumService));
		for (final ConfigurationDescription configuration : seleniumCoreConfig.getConfigurations()) {
			result.add(new ServiceInfo(ConfigurationDescription.class, configuration, configuration.getName()));
		}
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		serviceTrackers.add(new SeleniumConfigurationServiceTracker(seleniumConfigurationRegistry, context, SeleniumConfiguration.class));
		return serviceTrackers;
	}

	@Override
	protected void onStopped() throws Exception {
		super.onStopped();

		final Collection<SeleniumCoreWebDriver> webDrivers = seleniumCoreWebDriverRegistry.getAll();
		for (final SeleniumCoreWebDriver driver : webDrivers) {
			try {
				seleniumCoreWebDriverRegistry.remove(driver);
				driver.close();
			} catch (Exception e) {
				// nop
			}
		}
	}
}
