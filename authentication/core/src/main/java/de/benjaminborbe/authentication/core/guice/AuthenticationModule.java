package de.benjaminborbe.authentication.core.guice;

import com.google.inject.AbstractModule;
import javax.inject.Singleton;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.core.config.AuthenticationConfig;
import de.benjaminborbe.authentication.core.config.AuthenticationConfigImpl;
import de.benjaminborbe.authentication.core.dao.SessionDao;
import de.benjaminborbe.authentication.core.dao.SessionDaoStorage;
import de.benjaminborbe.authentication.core.dao.UserDao;
import de.benjaminborbe.authentication.core.dao.UserDaoStorage;
import de.benjaminborbe.authentication.core.service.AuthenticationServiceImpl;
import de.benjaminborbe.authentication.core.verifycredential.AuthenticationVerifyCredential;
import de.benjaminborbe.authentication.core.verifycredential.AuthenticationVerifyCredentialImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

public class AuthenticationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AuthenticationVerifyCredential.class).to(AuthenticationVerifyCredentialImpl.class).in(Singleton.class);
		bind(AuthenticationConfig.class).to(AuthenticationConfigImpl.class).in(Singleton.class);
		bind(UserDao.class).to(UserDaoStorage.class).in(Singleton.class);
		bind(SessionDao.class).to(SessionDaoStorage.class).in(Singleton.class);
		bind(AuthenticationService.class).to(AuthenticationServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);

		requestStaticInjection(AuthenticationValidatorLinker.class);
	}
}
