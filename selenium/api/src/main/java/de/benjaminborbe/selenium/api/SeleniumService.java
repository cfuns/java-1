package de.benjaminborbe.selenium.api;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfiguration;

import java.util.Collection;

public interface SeleniumService {

	void expectPermission(SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, SeleniumServiceException;

	boolean hasPermission(SessionIdentifier sessionIdentifier) throws SeleniumServiceException;

	SeleniumConfigurationIdentifier createSeleniumConfigurationIdentifier(String id) throws SeleniumServiceException;

	SeleniumExecutionProtocol execute(
		SessionIdentifier sessionIdentifier,
		SeleniumConfigurationIdentifier seleniumConfigurationIdentifier
	) throws SeleniumServiceException, LoginRequiredException, PermissionDeniedException;

	SeleniumExecutionProtocol execute(
		SessionIdentifier sessionIdentifier,
		SeleniumConfiguration seleniumConfiguration
	) throws SeleniumServiceException, LoginRequiredException, PermissionDeniedException;

	Collection<SeleniumConfiguration> getSeleniumConfigurations(final SessionIdentifier sessionIdentifier) throws SeleniumServiceException, LoginRequiredException, PermissionDeniedException;

	SeleniumConfiguration getConfiguration(
		SessionIdentifier sessionIdentifier,
		SeleniumConfigurationIdentifier seleniumConfigurationIdentifier
	) throws SeleniumServiceException, LoginRequiredException, PermissionDeniedException;

	void closeDrivers(SessionIdentifier sessionIdentifier) throws SeleniumServiceException, LoginRequiredException, PermissionDeniedException;

	SeleniumExecutionProtocol executeAction(
		SessionIdentifier sessionIdentifier,
		SeleniumActionConfiguration seleniumActionConfiguration
	) throws SeleniumServiceException, LoginRequiredException, PermissionDeniedException;
}
