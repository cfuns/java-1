package de.benjaminborbe.authorization.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.permission.PermissionDao;
import de.benjaminborbe.authorization.permission.PermissionDaoImpl;
import de.benjaminborbe.authorization.role.RoleDao;
import de.benjaminborbe.authorization.role.RoleDaoImpl;
import de.benjaminborbe.authorization.service.AuthorizationServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class AuthorizationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PermissionDao.class).to(PermissionDaoImpl.class).in(Singleton.class);
		bind(RoleDao.class).to(RoleDaoImpl.class).in(Singleton.class);
		bind(AuthorizationService.class).to(AuthorizationServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
