package de.benjaminborbe.authorization.gui.service;

import javax.inject.Inject;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.gui.AuthorizationGuiConstants;
import de.benjaminborbe.navigation.api.NavigationEntry;

public class AuthorizationGuiNavigationPermissionsEntry implements NavigationEntry {

	private final AuthorizationService authorizationService;

	@Inject
	public AuthorizationGuiNavigationPermissionsEntry(final AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@Override
	public String getTitle() {
		return "Permissions";
	}

	@Override
	public String getURL() {
		return "/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_PERMISSION_LIST;
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
