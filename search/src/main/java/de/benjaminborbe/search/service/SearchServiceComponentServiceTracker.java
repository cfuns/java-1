package de.benjaminborbe.search.service;

import org.osgi.framework.BundleContext;
import de.benjaminborbe.dashboard.service.RegistryServiceTracker;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.search.util.SearchServiceComponentRegistry;

public class SearchServiceComponentServiceTracker extends RegistryServiceTracker<SearchServiceComponent> {

	public SearchServiceComponentServiceTracker(
			final SearchServiceComponentRegistry registry,
			final BundleContext context,
			final Class<?> clazz) {
		super(registry, context, clazz);
	}

}
