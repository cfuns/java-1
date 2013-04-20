package de.benjaminborbe.cache;

import javax.inject.Inject;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.cache.guice.CacheModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CacheActivator extends BaseBundleActivator {

	@Inject
	private CacheService cacheService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new CacheModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(CacheService.class, cacheService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<>(super.getServiceTrackers(context));
		// serviceTrackers.add(new CacheServiceTracker(cacheRegistry, context,
		// CacheService.class));
		return serviceTrackers;
	}
}
