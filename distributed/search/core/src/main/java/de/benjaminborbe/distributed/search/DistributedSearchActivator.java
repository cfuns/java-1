package de.benjaminborbe.distributed.search;

import de.benjaminborbe.distributed.search.api.DistributedSearchService;
import de.benjaminborbe.distributed.search.guice.DistributedSearchModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DistributedSearchActivator extends BaseBundleActivator {

	@Inject
	private DistributedSearchService distributedIndexService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new DistributedSearchModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(DistributedSearchService.class, distributedIndexService));
		return result;
	}

}
