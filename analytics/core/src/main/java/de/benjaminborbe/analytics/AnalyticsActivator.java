package de.benjaminborbe.analytics;

import javax.inject.Inject;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.config.AnalyticsConfig;
import de.benjaminborbe.analytics.guice.AnalyticsModules;
import de.benjaminborbe.analytics.service.AnalyticsAggregationCronJob;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AnalyticsActivator extends BaseBundleActivator {

	@Inject
	private AnalyticsConfig analyticsConfig;

	@Inject
	private AnalyticsAggregationCronJob analyticsAggregationCronJob;

	@Inject
	private AnalyticsService analyticsService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new AnalyticsModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(AnalyticsService.class, analyticsService));
		result.add(new ServiceInfo(CronJob.class, analyticsAggregationCronJob, analyticsAggregationCronJob.getClass().getName()));
		for (final ConfigurationDescription configuration : analyticsConfig.getConfigurations()) {
			result.add(new ServiceInfo(ConfigurationDescription.class, configuration, configuration.getName()));
		}
		return result;
	}

}
