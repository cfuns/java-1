package de.benjaminborbe.cron;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.cron.api.CronController;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.cron.api.CronService;
import de.benjaminborbe.cron.guice.CronModules;
import de.benjaminborbe.cron.service.CronJobServiceTracker;
import de.benjaminborbe.cron.service.CronMessageConsumer;
import de.benjaminborbe.cron.util.CronJobRegistry;
import de.benjaminborbe.cron.util.CronManager;
import de.benjaminborbe.cron.util.Quartz;
import de.benjaminborbe.message.api.MessageConsumer;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class CronActivator extends BaseBundleActivator {

	@Inject
	private CronJobRegistry cronJobRegistry;

	@Inject
	private Quartz quartz;

	@Inject
	private CronController cronController;

	@Inject
	private CronService cronService;

	@Inject
	private CronMessageConsumer cronMessageConsumer;

	@Inject
	private CronManager cronManager;

	@Override
	protected void onStarted() {
		super.onStarted();
		cronManager.start();
	}

	@Override
	protected void onStopped() {
		cronManager.stop();
		super.onStopped();
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new CronModules(context);
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		serviceTrackers.add(new CronJobServiceTracker(logger, quartz, cronJobRegistry, context, CronJob.class));
		return serviceTrackers;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(CronController.class, cronController));
		result.add(new ServiceInfo(CronService.class, cronService));
		result.add(new ServiceInfo(MessageConsumer.class, cronMessageConsumer));
		return result;
	}

}
