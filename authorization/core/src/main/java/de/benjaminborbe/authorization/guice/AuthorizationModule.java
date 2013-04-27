package de.benjaminborbe.authorization.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.dao.PermissionDao;
import de.benjaminborbe.authorization.dao.PermissionDaoImpl;
import de.benjaminborbe.authorization.dao.RoleDao;
import de.benjaminborbe.authorization.dao.RoleDaoImpl;
import de.benjaminborbe.authorization.service.AuthorizationServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class AuthorizationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PermissionDao.class).to(PermissionDaoImpl.class).in(Singleton.class);
		bind(RoleDao.class).to(RoleDaoImpl.class).in(Singleton.class);
		bind(AuthorizationService.class).to(AuthorizationServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
