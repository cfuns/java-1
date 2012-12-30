package de.benjaminborbe.authentication.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.config.AuthenticationConfig;
import de.benjaminborbe.authentication.config.AuthenticationConfigImpl;
import de.benjaminborbe.authentication.dao.SessionDao;
import de.benjaminborbe.authentication.dao.SessionDaoStorage;
import de.benjaminborbe.authentication.dao.UserDao;
import de.benjaminborbe.authentication.dao.UserDaoStorage;
import de.benjaminborbe.authentication.service.AuthenticationServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class AuthenticationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AuthenticationConfig.class).to(AuthenticationConfigImpl.class).in(Singleton.class);
		bind(UserDao.class).to(UserDaoStorage.class).in(Singleton.class);
		bind(SessionDao.class).to(SessionDaoStorage.class).in(Singleton.class);
		bind(AuthenticationService.class).to(AuthenticationServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);

		requestStaticInjection(AuthenticationValidatorLinker.class);
	}
}
