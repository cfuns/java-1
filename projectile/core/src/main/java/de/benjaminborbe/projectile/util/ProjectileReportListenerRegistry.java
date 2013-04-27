package de.benjaminborbe.projectile.util;

import de.benjaminborbe.tools.registry.RegistryBase;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ProjectileReportListenerRegistry extends RegistryBase<ProjectileReportListener> {

	@Inject
	public ProjectileReportListenerRegistry(
		final ProjectileReportListenerDao projectileReportListenerDao,
		final ProjectileReportListenerAnalytics projectileReportListenerAnalytics
	) {
		super(projectileReportListenerDao, projectileReportListenerAnalytics);
	}

}
