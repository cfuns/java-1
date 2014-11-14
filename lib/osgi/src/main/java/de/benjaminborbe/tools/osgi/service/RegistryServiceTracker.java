package de.benjaminborbe.tools.osgi.service;

import de.benjaminborbe.tools.registry.Registry;
import org.osgi.framework.BundleContext;

public class RegistryServiceTracker<T> extends BaseServiceTracker<T> {

	private final Registry<T> registry;

	public RegistryServiceTracker(final Registry<T> registry, final BundleContext context, final Class<?> clazz) {
		super(context, clazz);
		this.registry = registry;
	}

	protected void serviceRemoved(final T service) {
		registry.remove(service);
	}

	protected void serviceAdded(final T service) {
		registry.add(service);
	}
}
