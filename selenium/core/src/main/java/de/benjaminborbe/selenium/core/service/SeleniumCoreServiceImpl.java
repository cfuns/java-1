package de.benjaminborbe.selenium.core.service;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.selenium.api.SeleniumExecutionProtocol;
import de.benjaminborbe.selenium.api.SeleniumService;
import de.benjaminborbe.selenium.api.SeleniumServiceException;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfiguration;
import de.benjaminborbe.selenium.core.SeleniumCoreConstatns;
import de.benjaminborbe.selenium.core.configuration.SeleniumConfigurationRegistry;
import de.benjaminborbe.selenium.core.util.SeleniumCoreActionExecutor;
import de.benjaminborbe.selenium.core.util.SeleniumCoreConfigurationExecutor;
import de.benjaminborbe.selenium.core.util.SeleniumCoreRunner;
import de.benjaminborbe.selenium.core.util.SeleniumCoreWebDriver;
import de.benjaminborbe.selenium.core.util.SeleniumCoreWebDriverRegistry;
import de.benjaminborbe.selenium.core.util.SeleniumExecutionProtocolImpl;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;

@Singleton
public class SeleniumCoreServiceImpl implements SeleniumService {

	private final Logger logger;

	private final SeleniumCoreActionExecutor seleniumCoreActionExecutor;

	private final SeleniumCoreConfigurationExecutor seleniumCoreConfigurationExecutor;

	private final AuthorizationService authorizationService;

	private final SeleniumConfigurationRegistry seleniumConfigurationRegistry;

	private final RunOnlyOnceATime runOnlyOnceATime;

	private final SeleniumCoreWebDriverRegistry seleniumCoreWebDriverRegistry;

	@Inject
	public SeleniumCoreServiceImpl(
		final Logger logger,
		final SeleniumCoreActionExecutor seleniumCoreActionExecutor,
		final SeleniumCoreConfigurationExecutor seleniumCoreConfigurationExecutor,
		final AuthorizationService authorizationService,
		final SeleniumConfigurationRegistry seleniumConfigurationRegistry,
		final RunOnlyOnceATime runOnlyOnceATime,
		final SeleniumCoreWebDriverRegistry seleniumCoreWebDriverRegistry
	) {
		this.logger = logger;
		this.seleniumCoreActionExecutor = seleniumCoreActionExecutor;
		this.seleniumCoreConfigurationExecutor = seleniumCoreConfigurationExecutor;
		this.authorizationService = authorizationService;
		this.seleniumConfigurationRegistry = seleniumConfigurationRegistry;
		this.runOnlyOnceATime = runOnlyOnceATime;
		this.seleniumCoreWebDriverRegistry = seleniumCoreWebDriverRegistry;
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
	public SeleniumExecutionProtocol execute(
		final SessionIdentifier sessionIdentifier, final SeleniumConfigurationIdentifier seleniumConfigurationIdentifier
	) throws SeleniumServiceException, LoginRequiredException, PermissionDeniedException {
		expectPermission(sessionIdentifier);

		final SeleniumConfiguration seleniumConfiguration = seleniumConfigurationRegistry.get(seleniumConfigurationIdentifier);
		return execute(seleniumConfiguration);
	}

	@Override
	public SeleniumExecutionProtocol execute(
		final SessionIdentifier sessionIdentifier,
		final SeleniumConfiguration seleniumConfiguration
	) throws SeleniumServiceException, LoginRequiredException, PermissionDeniedException {
		expectPermission(sessionIdentifier);
		return execute(seleniumConfiguration);
	}

	public SeleniumExecutionProtocol execute(
		final SeleniumConfiguration seleniumConfiguration
	) throws SeleniumServiceException {

		final SeleniumExecutionProtocolImpl seleniumExecutionProtocol = new SeleniumExecutionProtocolImpl();
		if (runOnlyOnceATime.run(new SeleniumCoreRunner(seleniumCoreConfigurationExecutor, seleniumConfiguration, seleniumExecutionProtocol))) {
			logger.trace("execute SeleniumConfiguration" + seleniumConfiguration.getId() + " completed");
		} else {
			final String msg = "execute SeleniumConfiguration " + seleniumConfiguration.getId() + " skipped, already running";
			logger.trace(msg);
			seleniumExecutionProtocol.addError(msg);
		}
		return seleniumExecutionProtocol;
	}

	@Override
	public Collection<SeleniumConfiguration> getSeleniumConfigurations(
		final SessionIdentifier sessionIdentifier
	) throws SeleniumServiceException, LoginRequiredException, PermissionDeniedException {
		expectPermission(sessionIdentifier);
		return new ArrayList<>(seleniumConfigurationRegistry.getAll());
	}

	@Override
	public SeleniumConfiguration getConfiguration(
		final SessionIdentifier sessionIdentifier,
		final SeleniumConfigurationIdentifier seleniumConfigurationIdentifier
	) throws SeleniumServiceException, LoginRequiredException, PermissionDeniedException {
		expectPermission(sessionIdentifier);
		return seleniumConfigurationRegistry.get(seleniumConfigurationIdentifier);
	}

	@Override
	public void closeDrivers(final SessionIdentifier sessionIdentifier) throws SeleniumServiceException, LoginRequiredException, PermissionDeniedException {
		expectPermission(sessionIdentifier);
		for (SeleniumCoreWebDriver driver : seleniumCoreWebDriverRegistry.getAll()) {
			if (!driver.isClosed()) {
				seleniumCoreWebDriverRegistry.remove(driver);
				try {
					driver.close();
				} catch (Exception e) {
					//nop
				}
			}
		}
	}

	@Override
	public SeleniumExecutionProtocol executeAction(
		final SessionIdentifier sessionIdentifier, final SeleniumActionConfiguration seleniumActionConfiguration
	) throws SeleniumServiceException, LoginRequiredException, PermissionDeniedException {
		expectPermission(sessionIdentifier);
		logger.debug("executeAction");

		final SeleniumExecutionProtocolImpl seleniumExecutionProtocol = new SeleniumExecutionProtocolImpl();
		final Collection<SeleniumCoreWebDriver> seleniumCoreWebDrivers = seleniumCoreWebDriverRegistry.getAll();
		logger.debug("found " + seleniumCoreWebDrivers.size() + " drivers");
		for (SeleniumCoreWebDriver driver : seleniumCoreWebDrivers) {
			if (!driver.isClosed() && driver.check()) {
				logger.debug("found open driver");
				if (seleniumCoreActionExecutor.execute(driver, seleniumActionConfiguration, seleniumExecutionProtocol)) {
					seleniumExecutionProtocol.addInfo("execute action completed");
					seleniumExecutionProtocol.complete();
					return seleniumExecutionProtocol;
				} else {
					seleniumExecutionProtocol.addError("execute action failed");
					return seleniumExecutionProtocol;
				}
			} else {
				logger.debug("driver closed");
				seleniumCoreWebDriverRegistry.remove(driver);
			}
		}
		return seleniumExecutionProtocol;
	}

}
