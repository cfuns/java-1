package de.benjaminborbe.selenium.core.service;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.selenium.api.SeleniumService;
import de.benjaminborbe.selenium.api.SeleniumServiceException;
import de.benjaminborbe.selenium.core.SeleniumCoreConstatns;
import de.benjaminborbe.selenium.core.configuration.SeleniumConfigurationRegistry;
import de.benjaminborbe.selenium.core.util.SeleniumCoreRunner;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class SeleniumCoreServiceImpl implements SeleniumService {

	private final Logger logger;

	private final SeleniumCoreRunner seleniumCoreRunner;

	private final AuthorizationService authorizationService;

	private final SeleniumConfigurationRegistry seleniumConfigurationRegistry;

	@Inject
	public SeleniumCoreServiceImpl(
		final Logger logger,
		final SeleniumCoreRunner seleniumCoreRunner,
		final AuthorizationService authorizationService,
		final SeleniumConfigurationRegistry seleniumConfigurationRegistry
	) {
		this.logger = logger;
		this.seleniumCoreRunner = seleniumCoreRunner;
		this.authorizationService = authorizationService;
		this.seleniumConfigurationRegistry = seleniumConfigurationRegistry;
	}

	@Override
	public void expectPermission(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, SeleniumServiceException {
		try {
			authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(SeleniumCoreConstatns.PERMISSION));
		} catch (final AuthorizationServiceException e) {
			throw new SeleniumServiceException(e);
		}
	}

	@Override
	public boolean hasPermission(final SessionIdentifier sessionIdentifier) throws SeleniumServiceException {
		try {
			return authorizationService.hasPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(SeleniumCoreConstatns.PERMISSION));
		} catch (final AuthorizationServiceException e) {
			throw new SeleniumServiceException(e);
		}
	}

	@Override
	public SeleniumConfigurationIdentifier createSeleniumConfigurationIdentifier(final String id) throws SeleniumServiceException {
		if (id != null && !id.trim().isEmpty()) {
			return new SeleniumConfigurationIdentifier(id.trim());
		}
		return null;
	}

	@Override
	public void run(
		final SessionIdentifier sessionIdentifier, final SeleniumConfigurationIdentifier seleniumConfigurationIdentifier
	) throws SeleniumServiceException, LoginRequiredException, PermissionDeniedException {
		logger.trace("run " + seleniumConfigurationIdentifier + " started");
		seleniumCoreRunner.run();
		logger.trace("run " + seleniumConfigurationIdentifier + " finished");
	}

	@Override
	public Collection<SeleniumConfiguration> getSeleniumConfigurations(final SessionIdentifier sessionIdentifier) throws SeleniumServiceException, LoginRequiredException, PermissionDeniedException {
		return seleniumConfigurationRegistry.getAll();
	}
}
