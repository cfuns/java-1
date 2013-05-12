package de.benjaminborbe.selenium.core;

import de.benjaminborbe.selenium.api.SeleniumService;
import de.benjaminborbe.selenium.core.guice.SeleniumCoreModules;
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
	private SeleniumService seleniumService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SeleniumCoreModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(SeleniumService.class, seleniumService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<>(super.getServiceTrackers(context));
		// serviceTrackers.add(new SeleniumServiceTracker(seleniumRegistry, context,
		// SeleniumService.class));
		return serviceTrackers;
	}
}
