package de.benjaminborbe.projectile.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.projectile.api.ProjectileService;
import de.benjaminborbe.projectile.config.ProjectileConfig;
import de.benjaminborbe.projectile.config.ProjectileConfigImpl;
import de.benjaminborbe.projectile.connector.ProjectileConnector;
import de.benjaminborbe.projectile.connector.ProjectileConnectorImpl;
import de.benjaminborbe.projectile.dao.ProjectileReportDao;
import de.benjaminborbe.projectile.dao.ProjectileReportDaoStorage;
import de.benjaminborbe.projectile.dao.ProjectileTeamDao;
import de.benjaminborbe.projectile.dao.ProjectileTeamDaoStorage;
import de.benjaminborbe.projectile.service.ProjectileServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class ProjectileModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(ProjectileTeamDao.class).to(ProjectileTeamDaoStorage.class).in(Singleton.class);
		bind(ProjectileReportDao.class).to(ProjectileReportDaoStorage.class).in(Singleton.class);
		bind(ProjectileConfig.class).to(ProjectileConfigImpl.class).in(Singleton.class);
		bind(ProjectileConnector.class).to(ProjectileConnectorImpl.class).in(Singleton.class);
		bind(ProjectileService.class).to(ProjectileServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);

		requestStaticInjection(ProjectileValidatorLinker.class);
	}
}
