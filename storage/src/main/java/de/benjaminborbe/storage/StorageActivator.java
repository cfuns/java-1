package de.benjaminborbe.storage;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import de.benjaminborbe.storage.api.CacheStorageService;
import de.benjaminborbe.storage.api.PersistentStorageService;
import de.benjaminborbe.storage.guice.StorageModules;
import de.benjaminborbe.storage.servlet.StorageDeleteServlet;
import de.benjaminborbe.storage.servlet.StorageListServlet;
import de.benjaminborbe.storage.servlet.StorageReadServlet;
import de.benjaminborbe.storage.servlet.StorageWriteServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class StorageActivator implements BundleActivator {

	private Injector injector;

	private ServiceTracker extHttpServiceTracker;

	@Inject
	private Logger logger;

	@Inject
	private GuiceFilter guiceFilter;

	@Inject
	private CacheStorageService cacheStorageService;

	@Inject
	private PersistentStorageService persistentStorageService;

	@Inject
	private StorageReadServlet storageReadServlet;

	@Inject
	private StorageDeleteServlet storageDeleteServlet;

	@Inject
	private StorageListServlet storageListServlet;

	@Inject
	private StorageWriteServlet storageWriteServlet;

	@Override
	public void start(final BundleContext context) throws Exception {
		try {
			getInjector(context);
			injector.injectMembers(this);

			{
				context.registerService(PersistentStorageService.class.getName(), persistentStorageService, null);
				context.registerService(CacheStorageService.class.getName(), cacheStorageService, null);
			}

			// create serviceTracker for ExtHttpService
			extHttpServiceTracker = new ServiceTracker(context, ExtHttpService.class.getName(), null) {

				@Override
				public Object addingService(final ServiceReference ref) {
					final Object service = super.addingService(ref);
					serviceAdded((ExtHttpService) service);
					return service;
				}

				@Override
				public void removedService(final ServiceReference ref, final Object service) {
					serviceRemoved((ExtHttpService) service);
					super.removedService(ref, service);
				}
			};
			extHttpServiceTracker.open();

		}
		catch (final Exception e) {
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
			final Injector injector = getInjector(context);
			injector.injectMembers(this);
			logger.info("stopping: " + this.getClass().getName() + " ...");

			// close tracker
			extHttpServiceTracker.close();

			logger.info("stopping: " + this.getClass().getName() + " done");
		}
		catch (final Exception e) {
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

	private Injector getInjector(final BundleContext context) {
		if (injector == null)
			injector = GuiceInjectorBuilder.getInjector(new StorageModules(context));
		return injector;
	}

	private void serviceAdded(final ExtHttpService service) {
		logger.debug("Activator.serviceAdded(ExtHttpService)");
		try {

			// filter
			service.registerFilter(guiceFilter, ".*", null, 999, null);

			// servlet
			service.registerServlet("/admin/storage/read", storageReadServlet, null, null);
			service.registerServlet("/admin/storage/delete", storageDeleteServlet, null, null);
			service.registerServlet("/admin/storage/write", storageWriteServlet, null, null);
			service.registerServlet("/admin/storage/list", storageListServlet, null, null);
		}
		catch (final Exception e) {
			logger.error("error during service activation", e);
		}
	}

	private void serviceRemoved(final ExtHttpService service) {
		logger.debug("Activator.serviceRemoved(ExtHttpService)");

		// filter
		service.unregisterFilter(guiceFilter);

		// servlet
		service.unregisterServlet(storageReadServlet);
		service.unregisterServlet(storageDeleteServlet);
		service.unregisterServlet(storageWriteServlet);
		service.unregisterServlet(storageListServlet);
	}

}
