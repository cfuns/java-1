package de.benjaminborbe.authentication.gui.service;

import javax.inject.Inject;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.gui.AuthenticationGuiConstants;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.navigation.api.NavigationEntry;

public class AuthenticationGuiNavigationEntry implements NavigationEntry {

	private final AuthorizationService authorizationService;

	@Inject
	public AuthenticationGuiNavigationEntry(final AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@Override
	public String getTitle() {
		return "Users";
	}

	@Override
	public String getURL() {
		return "/" + AuthenticationGuiConstants.NAME + AuthenticationGuiConstants.URL_USER_LIST;
	}

	@Override
	public boolean isVisible(final SessionIdentifier sessionIdentifier) {
		try {
			return authorizationService.hasAdminRole(sessionIdentifier);
		} catch (final AuthorizationServiceException e) {
			return false;
		}
	}

}
