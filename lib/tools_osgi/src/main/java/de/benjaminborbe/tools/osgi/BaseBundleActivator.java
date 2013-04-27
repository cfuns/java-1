package de.benjaminborbe.tools.osgi;

import com.google.inject.Injector;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.guice.Modules;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public abstract class BaseBundleActivator implements BundleActivator {

	@Inject
	protected Logger logger;

	private Injector injector;

	private final Set<ServiceRegistration> serviceRegistrations = new HashSet<>();

	private Injector getInjector(final BundleContext context) {
		if (getInjector() == null)
			injector = GuiceInjectorBuilder.getInjector(getModules(context));
		return getInjector();
	}

	@Override
	public void start(final BundleContext context) throws Exception {
		try {
			getInjector(context).injectMembers(this);
			logger.info("starting: " + this.getClass().getName() + "...");

			// register serviceTrackers
			for (final ServiceTracker serviceTracker : getServiceTrackers(context)) {
				serviceTracker.open();
			}

			// register services
			for (final ServiceInfo serviceInfo : getServiceInfos()) {
				final String name = serviceInfo.getName();
				final Object service = serviceInfo.getService();
				final Properties properties = serviceInfo.getProperties();
				serviceRegistrations.add(context.registerService(name, service, properties));
				logger.trace("register service with name: " + name + " service: " + service);
			}

			try {
				onStarted();
			} catch (final Exception e) {
				logger.debug("onStarted failed", e);
			}

			logger.info("starting: " + this.getClass().getName() + " done");
		} catch (final Exception e) {
			if (logger != null) {
				logger.error("starting: " + this.getClass().getName() + " failed: " + e.toString(), e);
			}
			// fallback if injector start fails!
			else {
				e.printStackTrace();
				System.out.println("starting: " + this.getClass().getName() + " failed: " + e.toString());
			}
			throw e;
		}
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		try {
			getInjector(context).injectMembers(this);
			logger.info("stopping: " + this.getClass().getName() + "...");

			// close servicetracker
			for (final ServiceTracker serviceTracker : getServiceTrackers(context)) {
				serviceTracker.close();
			}

			// unregister all services
			for (final ServiceRegistration serviceRegistration : serviceRegistrations) {
				serviceRegistration.unregister();
			}
			serviceRegistrations.clear();

			try {
				onStopped();
			} catch (final Exception e) {
				logger.debug("onStopped failed", e);
			}

			logger.info("stopping: " + this.getClass().getName() + " done");
		} catch (final Exception e) {
			if (logger != null) {
				logger.error("stopping: " + this.getClass().getName() + " failed: " + e.toString(), e);
			}
			// fallback if injector start fails!
			else {
				e.printStackTrace();
				System.out.println("stopping: " + this.getClass().getName() + " failed: " + e.toString());
			}
			throw e;
		}
	}

	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>();
		return result;
	}

	protected abstract Modules getModules(BundleContext context);

	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> result = new HashSet<>();
		return result;
	}

	protected void onStarted() {
	}

	protected void onStopped() {
	}

	public Injector getInjector() {
		return injector;
	}
}
