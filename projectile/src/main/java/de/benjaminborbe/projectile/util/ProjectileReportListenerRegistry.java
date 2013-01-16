package de.benjaminborbe.projectile.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.registry.RegistryBase;

@Singleton
public class ProjectileReportListenerRegistry extends RegistryBase<ProjectileReportListener> {

	@Inject
	public ProjectileReportListenerRegistry(final ProjectileReportListenerDao projectileReportListenerDao, final ProjectileReportListenerAnalytics projectileReportListenerAnalytics) {
		super(projectileReportListenerDao, projectileReportListenerAnalytics);
	}

}
