package de.benjaminborbe.cron;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.cron.guice.CronModules;
import de.benjaminborbe.cron.servlet.CronServlet;
import de.benjaminborbe.cron.util.CronJobRegistry;
import de.benjaminborbe.cron.util.Quartz;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class CronActivator implements BundleActivator {

	private Injector injector;

	private ServiceTracker extHttpServiceTracker;

	@Inject
	private CronJobRegistry cronJobRegistry;

	@Inject
	private Quartz quartz;

	@Inject
	private Logger logger;

	@Inject
	private GuiceFilter guiceFilter;

	@Inject
	private CronServlet cronServlet;

	private ServiceTracker cronJobServiceTracker;

	@Override
	public void start(final BundleContext context) throws Exception {
		try {
			getInjector(context);
			injector.injectMembers(this);
			logger.info("CronActivator.start() - starting: " + this.getClass().getName() + " ...");

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
			cronJobServiceTracker = new ServiceTracker(context, CronJob.class.getName(), null) {

				@Override
				public Object addingService(final ServiceReference ref) {
					final Object service = super.addingService(ref);
					serviceAdded((CronJob) service);
					return service;
				}

				@Override
				public void removedService(final ServiceReference ref, final Object service) {
					serviceRemoved((CronJob) service);
					super.removedService(ref, service);
				}
			};
			cronJobServiceTracker.open();

			// cron starten
			quartz.start();
		}
		catch (final Exception e) {
			if (logger != null) {
				logger.error("CronActivator.start() - starting: " + this.getClass().getName() + " failed: " + e.toString(), e);
			}
			// fallback if injector start fails!
			else {
				e.printStackTrace();
				System.out.println("CronActivator.start() - starting: " + this.getClass().getName() + " failed: "
						+ e.toString());
			}
			throw e;
		}

	}

	protected void serviceRemoved(final CronJob cronJob) {
		logger.debug("CronActivator.serviceRemoved() - CronJob removed " + cronJob.getClass().getName());
		cronJobRegistry.unregister(cronJob);
		quartz.removeCronJob(cronJob);
	}

	protected void serviceAdded(final CronJob cronJob) {
		logger.debug("CronActivator.serviceAdded() - CronJob added " + cronJob.getClass().getName());
		cronJobRegistry.register(cronJob);
		quartz.addCronJob(cronJob);
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		try {
			final Injector injector = getInjector(context);
			injector.injectMembers(this);
			logger.info("CronActivator.stop() - stopping: " + this.getClass().getName() + " ...");

			// close tracker
			extHttpServiceTracker.close();

			// close tracker
			cronJobServiceTracker.close();

			// cron stoppen
			quartz.stop();

			logger.info("CronActivator.stop() - stopping: " + this.getClass().getName() + " done");
		}
		catch (final Exception e) {
			if (logger != null) {
				logger.error("CronActivator.stop() - stopping: " + this.getClass().getName() + " failed: " + e.toString(), e);
			}
			// fallback if injector start fails!
			else {
				e.printStackTrace();
				System.out
						.println("CronActivator.stop() - stopping: " + this.getClass().getName() + " failed: " + e.toString());
			}
			throw e;
		}
	}

	protected Injector getInjector(final BundleContext context) {
		if (injector == null)
			injector = GuiceInjectorBuilder.getInjector(new CronModules(context));
		return injector;
	}

	protected void serviceAdded(final ExtHttpService service) {
		logger.debug("CronActivator.serviceAdded(ExtHttpService)");
		try {

			// filter
			service.registerFilter(guiceFilter, ".*", null, 999, null);

			// servlet
			service.registerServlet("/cron", cronServlet, null, null);
		}
		catch (final Exception e) {
			logger.error("error during service activation", e);
		}
	}

	protected void serviceRemoved(final ExtHttpService service) {
		logger.debug("CronActivator.serviceRemoved(ExtHttpService)");

		// filter
		service.unregisterFilter(guiceFilter);

		// servlet
		service.unregisterServlet(cronServlet);
	}

}
