package de.benjaminborbe.distributed.search;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.distributed.search.api.DistributedSearchService;
import de.benjaminborbe.distributed.search.guice.DistributedSearchModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class DistributedSearchActivator extends BaseBundleActivator {

	@Inject
	private DistributedSearchService distributedIndexService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new DistributedSearchModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(DistributedSearchService.class, distributedIndexService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new DistributedSearchServiceTracker(distributed_searchRegistry,
		// context,
		// DistributedSearchService.class));
		return serviceTrackers;
	}
}
