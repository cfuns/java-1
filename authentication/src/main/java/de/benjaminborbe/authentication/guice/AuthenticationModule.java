package de.benjaminborbe.authentication.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.service.AuthenticationServiceImpl;
import de.benjaminborbe.authentication.session.SessionDao;
import de.benjaminborbe.authentication.session.SessionDaoStorage;
import de.benjaminborbe.authentication.user.UserDao;
import de.benjaminborbe.authentication.user.UserDaoStorage;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class AuthenticationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(UserDao.class).to(UserDaoStorage.class).in(Singleton.class);
		bind(SessionDao.class).to(SessionDaoStorage.class).in(Singleton.class);
		bind(AuthenticationService.class).to(AuthenticationServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
