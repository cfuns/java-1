package de.benjaminborbe.authentication.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.service.AuthenticationServiceImpl;
import de.benjaminborbe.authentication.util.SessionDao;
import de.benjaminborbe.authentication.util.SessionDaoImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class AuthenticationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(SessionDao.class).to(SessionDaoImpl.class).in(Singleton.class);
		bind(AuthenticationService.class).to(AuthenticationServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
