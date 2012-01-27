package de.benjaminborbe.authorization.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authorization.api.AuthorizationService;

@Singleton
public class AuthorizationServiceImpl implements AuthorizationService {

	private final Logger logger;

	@Inject
	public AuthorizationServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.debug("execute");
	}

}
