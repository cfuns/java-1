package de.benjaminborbe.search;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;

import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.search.guice.SearchModules;
import de.benjaminborbe.search.servlet.SearchServlet;
import de.benjaminborbe.search.util.SearchServiceComponentRegistry;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class SearchActivator implements BundleActivator {

	private Injector injector;

	private ServiceTracker extHttpServiceTracker;

	@Inject
	private Logger logger;

	@Inject
	private GuiceFilter guiceFilter;

	@Inject
	private SearchServlet searchServlet;

	private ServiceTracker searchServiceTracker;

	@Inject
	private SearchServiceComponentRegistry searchServiceComponentRegistry;

	@Override
	public void start(final BundleContext context) throws Exception {
		try {
			getInjector(context);
			injector.injectMembers(this);

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

			// create serviceTracker for CronJob
			searchServiceTracker = new ServiceTracker(context, SearchServiceComponent.class.getName(), null) {

				@Override
				public Object addingService(final ServiceReference ref) {
					final Object service = super.addingService(ref);
					serviceAdded((SearchServiceComponent) service);
					return service;
				}

				@Override
				public void removedService(final ServiceReference ref, final Object service) {
					serviceRemoved((SearchServiceComponent) service);
					super.removedService(ref, service);
				}
			};
			searchServiceTracker.open();
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

	protected void serviceRemoved(final SearchServiceComponent service) {
		logger.debug("serviceRemoved: " + service);
		searchServiceComponentRegistry.unregister(service);
	}

	protected void serviceAdded(final SearchServiceComponent service) {
		logger.debug("serviceAdded: " + service);
		searchServiceComponentRegistry.register(service);
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
			injector = GuiceInjectorBuilder.getInjector(new SearchModules(context));
		return injector;
	}

	private void serviceAdded(final ExtHttpService service) {
		logger.debug("Activator.serviceAdded(ExtHttpService)");
		try {

			// filter
			service.registerFilter(guiceFilter, ".*", null, 999, null);

			// servlet
			service.registerServlet("/search", searchServlet, null, null);
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
		service.unregisterServlet(searchServlet);
	}

}
