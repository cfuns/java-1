package de.benjaminborbe.configuration.core;

import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.core.dao.ConfigurationRegistry;
import de.benjaminborbe.configuration.core.guice.ConfigurationModules;
import de.benjaminborbe.configuration.core.service.ConfigurationServiceTracker;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
		serviceTrackers.add(new ConfigurationServiceTracker(configurationRegistry, context));
		return serviceTrackers;
	}
}
