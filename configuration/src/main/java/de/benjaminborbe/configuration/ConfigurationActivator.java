package de.benjaminborbe.configuration;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.guice.ConfigurationModules;
import de.benjaminborbe.configuration.service.ConfigurationServiceTracker;
import de.benjaminborbe.configuration.util.ConfigurationRegistry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class ConfigurationActivator extends BaseBundleActivator {

	@Inject
	private ConfigurationService configurationService;

	@Inject
	private ConfigurationRegistry configurationRegistry;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new ConfigurationModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(ConfigurationService.class, configurationService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		serviceTrackers.add(new ConfigurationServiceTracker(configurationRegistry, context, ConfigurationDescription.class));
		return serviceTrackers;
	}
}
