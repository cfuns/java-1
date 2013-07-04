package de.benjaminborbe.navigation.service;

import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.navigation.util.NavigationEntryRegistry;
import de.benjaminborbe.tools.osgi.service.RegistryServiceTracker;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NavigationServiceTracker extends RegistryServiceTracker<NavigationEntry> {

	@Inject
	public NavigationServiceTracker(final NavigationEntryRegistry navigationEntryRegistry, final BundleContext context, final Class<?> clazz) {
		super(navigationEntryRegistry, context, clazz);
	}

}
