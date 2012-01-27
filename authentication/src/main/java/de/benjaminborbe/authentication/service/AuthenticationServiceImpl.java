package de.benjaminborbe.authentication.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;

@Singleton
public class AuthenticationServiceImpl implements AuthenticationService {

	private final Logger logger;

	@Inject
	public AuthenticationServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.debug("execute");
	}

}
