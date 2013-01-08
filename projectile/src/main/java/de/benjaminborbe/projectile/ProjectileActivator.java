package de.benjaminborbe.projectile;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.projectile.api.ProjectileService;
import de.benjaminborbe.projectile.config.ProjectileConfig;
import de.benjaminborbe.projectile.guice.ProjectileModules;
import de.benjaminborbe.projectile.service.ProjectileMailReportFetcherCronJob;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class ProjectileActivator extends BaseBundleActivator {

	@Inject
	private ProjectileService projectileService;

	@Inject
	private ProjectileConfig projectileConfig;

	@Inject
	private ProjectileMailReportFetcherCronJob projectileFetchMailReportCronJob;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new ProjectileModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(CronJob.class, projectileFetchMailReportCronJob, projectileFetchMailReportCronJob.getClass().getName()));
		result.add(new ServiceInfo(ProjectileService.class, projectileService));
		for (final ConfigurationDescription configuration : projectileConfig.getConfigurations()) {
			result.add(new ServiceInfo(ConfigurationDescription.class, configuration, configuration.getName()));
		}
		return result;
	}

}
