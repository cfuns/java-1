package de.benjaminborbe.dhl;

import com.google.inject.Inject;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.dhl.api.DhlService;
import de.benjaminborbe.dhl.guice.DhlModules;
import de.benjaminborbe.dhl.service.DhlStatusCheckCronJob;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DhlActivator extends BaseBundleActivator {

	@Inject
	private DhlService dhlService;

	@Inject
	private DhlStatusCheckCronJob dhlCheckCronJob;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new DhlModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(DhlService.class, dhlService));
		result.add(new ServiceInfo(CronJob.class, dhlCheckCronJob, dhlCheckCronJob.getClass().getName()));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<>(super.getServiceTrackers(context));
		// serviceTrackers.add(new DhlServiceTracker(dhlRegistry, context,
		// DhlService.class));
		return serviceTrackers;
	}
}
