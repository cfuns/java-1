package de.benjaminborbe.search.core.service;

import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.search.core.util.SearchServiceComponentRegistry;
import de.benjaminborbe.tools.osgi.service.RegistryServiceTracker;
import org.osgi.framework.BundleContext;

public class SearchServiceComponentServiceTracker extends RegistryServiceTracker<SearchServiceComponent> {

	public SearchServiceComponentServiceTracker(final SearchServiceComponentRegistry registry, final BundleContext context, final Class<?> clazz) {
		super(registry, context, clazz);
	}

}
