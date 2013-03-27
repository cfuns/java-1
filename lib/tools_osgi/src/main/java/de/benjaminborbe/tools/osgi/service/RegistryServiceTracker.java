package de.benjaminborbe.tools.osgi.service;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import de.benjaminborbe.tools.registry.Registry;

public class RegistryServiceTracker<T> extends ServiceTracker {

	private final Registry<T> registry;

	public RegistryServiceTracker(final Registry<T> registry, final BundleContext context, final Class<?> clazz) {
		super(context, clazz.getName(), null);
		this.registry = registry;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object addingService(final ServiceReference ref) {
		final Object service = super.addingService(ref);
		serviceAdded((T) service);
		return service;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removedService(final ServiceReference ref, final Object service) {
		serviceRemoved((T) service);
		super.removedService(ref, service);
	}

	protected void serviceRemoved(final T service) {
		registry.remove(service);
	}

	protected void serviceAdded(final T service) {
		registry.add(service);
	}
}
