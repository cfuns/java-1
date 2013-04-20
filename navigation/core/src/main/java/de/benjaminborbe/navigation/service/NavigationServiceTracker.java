package de.benjaminborbe.navigation.service;

import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.navigation.util.NavigationEntryRegistry;
import de.benjaminborbe.tools.osgi.service.RegistryServiceTracker;

@Singleton
public class NavigationServiceTracker extends RegistryServiceTracker<NavigationEntry> {

	@Inject
	public NavigationServiceTracker(final NavigationEntryRegistry navigationEntryRegistry, final BundleContext context, final Class<?> clazz) {
		super(navigationEntryRegistry, context, clazz);
	}

}
