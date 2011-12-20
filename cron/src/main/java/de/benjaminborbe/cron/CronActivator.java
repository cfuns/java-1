package de.benjaminborbe.cron;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.quartz.SchedulerException;

import com.google.inject.Inject;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.cron.guice.CronModules;
import de.benjaminborbe.cron.servlet.CronServlet;
import de.benjaminborbe.cron.util.CronJobRegistry;
import de.benjaminborbe.cron.util.Quartz;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class CronActivator extends HttpBundleActivator {

	@Inject
	private CronJobRegistry cronJobRegistry;

	@Inject
	private Quartz quartz;

	@Inject
	private CronServlet cronServlet;

	public CronActivator() {
		super("cron");
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
	protected void onStarted() {
		// cron stoppen
		try {
			quartz.start();
		}
		catch (final SchedulerException e) {
			logger.error("SchedulerException", e);
		}
	}

	@Override
	protected void onStopped() {
		// cron stoppen
		try {
			quartz.stop();
		}
		catch (final SchedulerException e) {
			logger.error("SchedulerException", e);
		}
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>();
		result.add(new ServletInfo(cronServlet, "/"));
		return result;
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new CronModules(context);
	}

	@Override
	protected Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// create serviceTracker for CronJob
		{
			final ServiceTracker serviceTracker = new ServiceTracker(context, CronJob.class.getName(), null) {

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
			serviceTrackers.add(serviceTracker);
		}
		return serviceTrackers;
	}
}
