package de.benjaminborbe.search.gui.util;

import javax.inject.Inject;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.search.gui.SearchGuiConstants;

public class SearchGuiNavigationEntry implements NavigationEntry {

	private final AuthenticationService authenticationService;

	@Inject
	public SearchGuiNavigationEntry(final AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@Override
	public String getTitle() {
		return "Search";
	}

	@Override
	public String getURL() {
		return "/" + SearchGuiConstants.NAME;
	}

	@Override
	public boolean isVisible(final SessionIdentifier sessionIdentifier) {
		try {
			return authenticationService.isLoggedIn(sessionIdentifier);
		}
		catch (final AuthenticationServiceException e) {
			return false;
		}
	}

}
