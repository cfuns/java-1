package de.benjaminborbe.index;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;

import de.benjaminborbe.index.guice.IndexModules;
import de.benjaminborbe.index.servlet.GoServlet;
import de.benjaminborbe.index.servlet.IndexServlet;
import de.benjaminborbe.index.servlet.SearchServlet;
import de.benjaminborbe.index.servlet.TwentyfeetPerformanceServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class IndexActivator implements BundleActivator {

	private Injector injector;

	private ServiceTracker extHttpServiceTracker;

	@Inject
	private Logger logger;

	@Inject
	private GuiceFilter guiceFilter;

	@Inject
	private IndexServlet indexServlet;

	@Inject
	private SearchServlet searchServlet;

	@Inject
	private TwentyfeetPerformanceServlet twentyfeetPerformanceServlet;

	@Inject
	private GoServlet goServlet;

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
			injector = GuiceInjectorBuilder.getInjector(new IndexModules(context));
		return injector;
	}

	private void serviceAdded(final ExtHttpService service) {
		logger.debug("Activator.serviceAdded(ExtHttpService)");
		try {

			// filter
			service.registerFilter(guiceFilter, ".*", null, 999, null);

			// resources
			service.registerResources("/js", "js", null);
			service.registerResources("/css", "css", null);

			// servlet
			service.registerServlet("/", indexServlet, null, null);
			service.registerServlet("/search", searchServlet, null, null);
			service.registerServlet("/go", goServlet, null, null);
			service.registerServlet("/twentyfeetperformance", twentyfeetPerformanceServlet, null, null);
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
		service.unregisterServlet(indexServlet);
		service.unregisterServlet(searchServlet);
		service.unregisterServlet(goServlet);
		service.unregisterServlet(twentyfeetPerformanceServlet);
	}

}