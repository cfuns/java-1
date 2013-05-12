package de.benjaminborbe.selenium.mock;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.selenium.api.SeleniumService;
import de.benjaminborbe.selenium.api.SeleniumServiceException;

import java.util.Collection;

public class SeleniumServiceMock implements SeleniumService {

	public SeleniumServiceMock() {
	}

	@Override
	public void expectPermission(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, SeleniumServiceException {
	}

	@Override
	public boolean hasPermission(final SessionIdentifier sessionIdentifier) throws SeleniumServiceException {
		return true;
	}

	@Override
	public SeleniumConfigurationIdentifier createSeleniumConfigurationIdentifier(final String id) throws SeleniumServiceException {
		return null;
	}

	@Override
	public void run(
		final SessionIdentifier sessionIdentifier, final SeleniumConfigurationIdentifier seleniumConfigurationIdentifier
	) throws SeleniumServiceException, LoginRequiredException, PermissionDeniedException {
	}

	@Override
	public Collection<SeleniumConfiguration> getSeleniumConfigurations(final SessionIdentifier sessionIdentifier) throws SeleniumServiceException, LoginRequiredException, PermissionDeniedException {
		return null;
	}
}
