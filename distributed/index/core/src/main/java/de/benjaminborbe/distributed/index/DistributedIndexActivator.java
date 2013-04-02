package de.benjaminborbe.distributed.index;

import com.google.inject.Inject;
import de.benjaminborbe.distributed.index.api.DistributedIndexService;
import de.benjaminborbe.distributed.index.guice.DistributedIndexModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DistributedIndexActivator extends BaseBundleActivator {

	@Inject
	private DistributedIndexService distributedIndexService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new DistributedIndexModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(DistributedIndexService.class, distributedIndexService));
		return result;
	}

}
