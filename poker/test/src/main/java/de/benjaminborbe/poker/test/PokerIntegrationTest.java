package de.benjaminborbe.poker.test;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.test.osgi.TestCaseOsgi;

public abstract class PokerIntegrationTest extends TestCaseOsgi {

	protected PokerService getPokerService() {
		final Object serviceObject = getServiceObject(PokerService.class.getName(), null);
		return (PokerService) serviceObject;
	}

	protected ConfigurationService getConfigurationService() {
		final Object serviceObject = getServiceObject(ConfigurationService.class.getName(), null);
		return (ConfigurationService) serviceObject;
	}

	protected AuthenticationService getAuthenticationService() {
		final Object serviceObject = getServiceObject(AuthenticationService.class.getName(), null);
		return (AuthenticationService) serviceObject;
	}
}
