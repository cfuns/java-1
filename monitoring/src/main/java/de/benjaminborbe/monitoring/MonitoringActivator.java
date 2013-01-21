package de.benjaminborbe.monitoring;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.config.MonitoringConfig;
import de.benjaminborbe.monitoring.guice.MonitoringModules;
import de.benjaminborbe.monitoring.service.MonitoringCronJob;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class MonitoringActivator extends BaseBundleActivator {

	@Inject
	private MonitoringCronJob monitoringCheckCronJob;

	@Inject
	private MonitoringService monitoringService;

	@Inject
	private MonitoringConfig monitoringConfig;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new MonitoringModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(CronJob.class, monitoringCheckCronJob, monitoringCheckCronJob.getClass().getName()));
		result.add(new ServiceInfo(MonitoringService.class, monitoringService));
		for (final ConfigurationDescription configuration : monitoringConfig.getConfigurations()) {
			result.add(new ServiceInfo(ConfigurationDescription.class, configuration, configuration.getName()));
		}
		return result;
	}

}
