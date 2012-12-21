package de.benjaminborbe.configuration.gui.util;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.configuration.gui.ConfigurationGuiConstants;
import de.benjaminborbe.navigation.api.NavigationEntry;

public class ConfigurationGuiNavigationEntry implements NavigationEntry {

	private final AuthenticationService authenticationService;

	@Inject
	public ConfigurationGuiNavigationEntry(final AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
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
			return authenticationService.isSuperAdmin(sessionIdentifier);
		}
		catch (final AuthenticationServiceException e) {
			return false;
		}
	}

}
