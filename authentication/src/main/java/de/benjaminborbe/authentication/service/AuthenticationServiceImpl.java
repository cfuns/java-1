package de.benjaminborbe.authentication.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;

@Singleton
public class AuthenticationServiceImpl implements AuthenticationService {

	private final Logger logger;

	private final String USERNAME = "bborbe";

	private final String PASSWORD = "test123";

	@Inject
	public AuthenticationServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public boolean verifyCredential(final String username, final String password) {
		logger.debug("execute");
		return USERNAME.equals(username) && PASSWORD.equals(password);
	}

}
