package de.benjaminborbe.search.service;

import org.osgi.framework.BundleContext;

import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.search.util.SearchServiceComponentRegistry;
import de.benjaminborbe.tools.osgi.service.RegistryServiceTracker;

public class SearchServiceComponentServiceTracker extends RegistryServiceTracker<SearchServiceComponent> {

	public SearchServiceComponentServiceTracker(final SearchServiceComponentRegistry registry, final BundleContext context, final Class<?> clazz) {
		super(registry, context, clazz);
	}

}
