package de.benjaminborbe.confluence.gui.util;

import javax.inject.Inject;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.confluence.gui.ConfluenceGuiConstants;
import de.benjaminborbe.navigation.api.NavigationEntry;

public class ConfluenceGuiNavigationEntry implements NavigationEntry {

	private final AuthorizationService authorizationService;

	@Inject
	public ConfluenceGuiNavigationEntry(final AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@Override
	public String getTitle() {
		return "Confluence";
	}

	@Override
	public String getURL() {
		return "/" + ConfluenceGuiConstants.NAME;
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
