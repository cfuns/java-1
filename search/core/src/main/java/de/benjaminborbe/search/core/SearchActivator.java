package de.benjaminborbe.search.core;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.search.api.SearchService;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.search.core.config.SearchConfig;
import de.benjaminborbe.search.core.guice.SearchModules;
import de.benjaminborbe.search.core.service.SearchServiceComponentServiceTracker;
import de.benjaminborbe.search.core.util.SearchServiceComponentRegistry;
import de.benjaminborbe.search.core.util.UrlSearchServiceComponent;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SearchActivator extends BaseBundleActivator {

	@Inject
	private SearchService searchService;

	@Inject
	private SearchServiceComponentRegistry searchServiceComponentRegistry;

	@Inject
	private UrlSearchServiceComponent urlSearchServiceComponent;

	@Inject
	private SearchConfig searchConfig;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SearchModules(context);
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<>(super.getServiceTrackers(context));
		serviceTrackers.add(new SearchServiceComponentServiceTracker(searchServiceComponentRegistry, context, SearchServiceComponent.class));
		return serviceTrackers;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(SearchService.class, searchService));
		result.add(new ServiceInfo(SearchServiceComponent.class, urlSearchServiceComponent, urlSearchServiceComponent.getClass().getName()));
		for (final ConfigurationDescription configuration : searchConfig.getConfigurations()) {
			result.add(new ServiceInfo(ConfigurationDescription.class, configuration, configuration.getName()));
		}
		return result;
	}
}
