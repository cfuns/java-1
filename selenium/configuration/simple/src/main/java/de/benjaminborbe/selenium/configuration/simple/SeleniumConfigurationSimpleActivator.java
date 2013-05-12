package de.benjaminborbe.selenium.configuration.simple;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.selenium.configuration.simple.guice.SeleniumConfigurationSimpleModules;
import de.benjaminborbe.selenium.configuration.simple.service.SeleniumConfigurationSimple;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SeleniumConfigurationSimpleActivator extends BaseBundleActivator {

	@Inject
	private SeleniumConfigurationSimple seleniumConfigurationSimple;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SeleniumConfigurationSimpleModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(SeleniumConfiguration.class, seleniumConfigurationSimple));
		return result;
	}
}
