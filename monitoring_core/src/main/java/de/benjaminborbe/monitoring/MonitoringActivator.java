package de.benjaminborbe.monitoring;

import com.google.inject.Inject;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.check.MonitoringCheckRegistry;
import de.benjaminborbe.monitoring.config.MonitoringConfig;
import de.benjaminborbe.monitoring.guice.MonitoringModules;
import de.benjaminborbe.monitoring.service.MonitoringCheckCronJob;
import de.benjaminborbe.monitoring.service.MonitoringCheckServiceTracker;
import de.benjaminborbe.monitoring.service.MonitoringMailCronJob;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MonitoringActivator extends BaseBundleActivator {

	@Inject
	private MonitoringCheckCronJob monitoringCheckCronJob;

	@Inject
	private MonitoringMailCronJob monitoringMailCronJob;

	@Inject
	private MonitoringService monitoringService;

	@Inject
	private MonitoringConfig monitoringConfig;

	@Inject
	private MonitoringCheckRegistry monitoringCheckRegistry;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new MonitoringModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(CronJob.class, monitoringCheckCronJob, monitoringCheckCronJob.getClass().getName()));
		result.add(new ServiceInfo(CronJob.class, monitoringMailCronJob, monitoringMailCronJob.getClass().getName()));
		result.add(new ServiceInfo(MonitoringService.class, monitoringService));
		for (final ConfigurationDescription configuration : monitoringConfig.getConfigurations()) {
			result.add(new ServiceInfo(ConfigurationDescription.class, configuration, configuration.getName()));
		}
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		serviceTrackers.add(new MonitoringCheckServiceTracker(monitoringCheckRegistry, context, MonitoringCheck.class));
		return serviceTrackers;
	}
}
