package de.benjaminborbe.selenium.api;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

import java.util.Collection;

public interface SeleniumService {

	void expectPermission(SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, SeleniumServiceException;

	boolean hasPermission(SessionIdentifier sessionIdentifier) throws SeleniumServiceException;

	SeleniumConfigurationIdentifier createSeleniumConfigurationIdentifier(String id) throws SeleniumServiceException;

	boolean execute(
		SessionIdentifier sessionIdentifier,
		SeleniumConfigurationIdentifier seleniumConfigurationIdentifier
	) throws SeleniumServiceException, LoginRequiredException, PermissionDeniedException;

	Collection<SeleniumConfiguration> getSeleniumConfigurations(final SessionIdentifier sessionIdentifier) throws SeleniumServiceException, LoginRequiredException, PermissionDeniedException;
}
