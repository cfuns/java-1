package de.benjaminborbe.distributed.index;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.distributed.index.api.DistributedIndexService;
import de.benjaminborbe.distributed.index.guice.DistributedIndexModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class DistributedIndexActivator extends BaseBundleActivator {

	@Inject
	private DistributedIndexService distributedIndexService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new DistributedIndexModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(DistributedIndexService.class, distributedIndexService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new DistributedIndexServiceTracker(distributed_indexRegistry,
		// context,
		// DistributedIndexService.class));
		return serviceTrackers;
	}
}
