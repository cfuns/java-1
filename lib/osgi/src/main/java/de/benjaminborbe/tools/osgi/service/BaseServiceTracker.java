package de.benjaminborbe.tools.osgi.service;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

public abstract class BaseServiceTracker<T> extends ServiceTracker {

	public BaseServiceTracker(final BundleContext context, final Class<?> clazz) {
		super(context, clazz.getName(), null);
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

	abstract protected void serviceRemoved(final T service);

	abstract protected void serviceAdded(final T service);

}
