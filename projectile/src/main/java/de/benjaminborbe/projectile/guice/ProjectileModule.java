package de.benjaminborbe.projectile.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.projectile.api.ProjectileService;
import de.benjaminborbe.projectile.connector.ProjectileConnector;
import de.benjaminborbe.projectile.connector.ProjectileConnectorImpl;
import de.benjaminborbe.projectile.service.ProjectileServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class ProjectileModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ProjectileConnector.class).to(ProjectileConnectorImpl.class).in(Singleton.class);
		bind(ProjectileService.class).to(ProjectileServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
