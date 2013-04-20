package de.benjaminborbe.navigation;

import javax.inject.Inject;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.navigation.api.NavigationService;
import de.benjaminborbe.navigation.guice.NavigationModules;
import de.benjaminborbe.navigation.service.NavigationServiceTracker;
import de.benjaminborbe.navigation.util.NavigationEntryRegistry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class NavigationActivator extends BaseBundleActivator {

	@Inject
	private NavigationEntryRegistry navigationEntryRegistry;

	@Inject
	private NavigationService navigationService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new NavigationModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationService.class, navigationService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<>(super.getServiceTrackers(context));
		serviceTrackers.add(new NavigationServiceTracker(navigationEntryRegistry, context, NavigationEntry.class));
		return serviceTrackers;
	}
}
