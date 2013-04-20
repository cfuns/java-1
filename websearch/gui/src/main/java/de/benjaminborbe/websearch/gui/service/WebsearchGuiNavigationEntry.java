package de.benjaminborbe.websearch.gui.service;

import javax.inject.Inject;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.websearch.gui.WebsearchGuiConstants;

public class WebsearchGuiNavigationEntry implements NavigationEntry {

	private final AuthorizationService authorizationService;

	@Inject
	public WebsearchGuiNavigationEntry(final AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@Override
	public String getTitle() {
		return "Websearch";
	}

	@Override
	public String getURL() {
		return "/" + WebsearchGuiConstants.NAME;
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
