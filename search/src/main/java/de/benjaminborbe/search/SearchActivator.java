package de.benjaminborbe.search;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.search.api.SearchService;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.search.guice.SearchModules;
import de.benjaminborbe.search.service.SearchServiceComponentServiceTracker;
import de.benjaminborbe.search.util.SearchServiceComponentRegistry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class SearchActivator extends BaseBundleActivator {

	@Inject
	private SearchService searchService;

	@Inject
	private SearchServiceComponentRegistry searchServiceComponentRegistry;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SearchModules(context);
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		serviceTrackers.add(new SearchServiceComponentServiceTracker(searchServiceComponentRegistry, context, SearchServiceComponent.class));
		return serviceTrackers;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(SearchService.class, searchService));
		return result;
	}
}
