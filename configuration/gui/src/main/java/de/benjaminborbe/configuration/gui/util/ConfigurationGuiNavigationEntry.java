package de.benjaminborbe.configuration.gui.util;

import javax.inject.Inject;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.configuration.gui.ConfigurationGuiConstants;
import de.benjaminborbe.navigation.api.NavigationEntry;

public class ConfigurationGuiNavigationEntry implements NavigationEntry {

	private final AuthorizationService authorizationService;

	@Inject
	public ConfigurationGuiNavigationEntry(final AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@Override
	public String getTitle() {
		return "Configuration";
	}

	@Override
	public String getURL() {
		return "/" + ConfigurationGuiConstants.NAME + ConfigurationGuiConstants.URL_LIST;
	}

	@Override
	public boolean isVisible(final SessionIdentifier sessionIdentifier) {
		try {
			return authorizationService.hasAdminRole(sessionIdentifier);
		}
		catch (final AuthorizationServiceException e) {
			return false;
		}
	}

}
