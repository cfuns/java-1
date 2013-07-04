package de.benjaminborbe.search.gui.service;

import de.benjaminborbe.search.api.SearchSpecial;
import de.benjaminborbe.tools.osgi.service.RegistryServiceTracker;
import org.osgi.framework.BundleContext;

import javax.inject.Singleton;

@Singleton
public class SearchGuiSpecialSearchServiceTracker extends RegistryServiceTracker<SearchSpecial> {

	public SearchGuiSpecialSearchServiceTracker(final SearchGuiSpecialSearchRegistry registry, final BundleContext context, final Class<?> clazz) {
		super(registry, context, clazz);

	}

}
