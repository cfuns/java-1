package de.benjaminborbe.search.gui.service;

import org.osgi.framework.BundleContext;
import com.google.inject.Singleton;

import de.benjaminborbe.search.api.SearchSpecial;
import de.benjaminborbe.tools.osgi.service.RegistryServiceTracker;

@Singleton
public class SearchGuiSpecialSearchServiceTracker extends RegistryServiceTracker<SearchSpecial> {

	public SearchGuiSpecialSearchServiceTracker(final SearchGuiSpecialSearchRegistry registry, final BundleContext context, final Class<?> clazz) {
		super(registry, context, clazz);

	}

}
