package de.benjaminborbe.lunch.gui.util;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.lunch.gui.LunchGuiConstants;
import de.benjaminborbe.navigation.api.NavigationEntry;

public class LunchGuiNotificationNavigationEntry implements NavigationEntry {

	private final AuthenticationService authenticationService;

	@Inject
	public LunchGuiNotificationNavigationEntry(final AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@Override
	public String getTitle() {
		return "Mittagessen-Notification";
	}

	@Override
	public String getURL() {
		return "/" + LunchGuiConstants.NAME + LunchGuiConstants.URL_NOTIFICATION;
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
