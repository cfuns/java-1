package de.benjaminborbe.util.gui.service;

import javax.inject.Inject;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.util.gui.UtilGuiConstants;

public class UtilGuiNavigationEntry implements NavigationEntry {

	private final AuthorizationService authorizationService;

	@Inject
	public UtilGuiNavigationEntry(final AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@Override
	public String getTitle() {
		return "Util";
	}

	@Override
	public String getURL() {
		return "/" + UtilGuiConstants.NAME;
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
