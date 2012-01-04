package de.benjaminborbe.navigation.service;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.dashboard.service.RegistryServiceTracker;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.navigation.util.NavigationEntryRegistry;

@Singleton
public class NavigationServiceTracker extends RegistryServiceTracker<NavigationEntry> {

	@Inject
	public NavigationServiceTracker(
			final NavigationEntryRegistry navigationEntryRegistry,
			final BundleContext context,
			final Class<?> clazz) {
		super(navigationEntryRegistry, context, clazz);
	}

}
