package de.benjaminborbe.projectile.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.projectile.api.ProjectileService;
import de.benjaminborbe.projectile.config.ProjectileConfig;
import de.benjaminborbe.projectile.config.ProjectileConfigImpl;
import de.benjaminborbe.projectile.connector.ProjectileConnector;
import de.benjaminborbe.projectile.connector.ProjectileConnectorImpl;
import de.benjaminborbe.projectile.dao.ProjectileReportDao;
import de.benjaminborbe.projectile.dao.ProjectileReportDaoStorage;
import de.benjaminborbe.projectile.service.ProjectileServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class ProjectileModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ProjectileReportDao.class).to(ProjectileReportDaoStorage.class).in(Singleton.class);
		bind(ProjectileConfig.class).to(ProjectileConfigImpl.class).in(Singleton.class);
		bind(ProjectileConnector.class).to(ProjectileConnectorImpl.class).in(Singleton.class);
		bind(ProjectileService.class).to(ProjectileServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
