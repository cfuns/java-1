package de.benjaminborbe.authorization.gui.service;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.gui.AuthorizationGuiConstants;
import de.benjaminborbe.navigation.api.NavigationEntry;

public class AuthorizationGuiNavigationEntry implements NavigationEntry {

	private final AuthenticationService authenticationService;

	@Inject
	public AuthorizationGuiNavigationEntry(final AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@Override
	public String getTitle() {
		return "Roles";
	}

	@Override
	public String getURL() {
		return "/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_ROLE_LIST;
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
