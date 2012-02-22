package de.benjaminborbe.microblog;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.microblog.api.MicroblogService;
import de.benjaminborbe.microblog.guice.MicroblogModules;
import de.benjaminborbe.microblog.service.MicroblogCronJob;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class MicroblogActivator extends BaseBundleActivator {

	@Inject
	private MicroblogCronJob microblogCronJob;

	@Inject
	private MicroblogService microblogService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new MicroblogModules(context);
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new MicroblogServiceTracker(microblogRegistry, context,
		// MicroblogService.class));
		return serviceTrackers;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(CronJob.class, microblogCronJob, microblogCronJob.getClass().getName()));
		result.add(new ServiceInfo(MicroblogService.class, microblogService));
		return result;
	}
}
