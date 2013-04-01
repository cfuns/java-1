package de.benjaminborbe.streamcache;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.streamcache.api.StreamcacheService;
import de.benjaminborbe.streamcache.guice.StreamcacheModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class StreamcacheActivator extends BaseBundleActivator {

	@Inject
	private StreamcacheService streamcacheService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new StreamcacheModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(StreamcacheService.class, streamcacheService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new StreamcacheServiceTracker(streamcacheRegistry, context,
		// StreamcacheService.class));
		return serviceTrackers;
	}
}
