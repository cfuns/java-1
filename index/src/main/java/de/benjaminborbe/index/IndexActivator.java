package de.benjaminborbe.index;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.index.api.IndexSearcherService;
import de.benjaminborbe.index.api.IndexerService;
import de.benjaminborbe.index.guice.IndexModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class IndexActivator extends BaseBundleActivator {

	@Inject
	private IndexerService indexerService;

	@Inject
	private IndexSearcherService indexSearcherService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new IndexModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(IndexerService.class, indexerService));
		result.add(new ServiceInfo(IndexSearcherService.class, indexSearcherService));
		return result;
	}
}
