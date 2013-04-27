package de.benjaminborbe.notification.gui.service;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.notification.gui.NotificationGuiConstants;

import javax.inject.Inject;

public class NotificationGuiNavigationEntry implements NavigationEntry {

	private final AuthenticationService authenticationService;

	@Inject
	public NotificationGuiNavigationEntry(final AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@Override
	public String getTitle() {
		return "Notification";
	}

	@Override
	public String getURL() {
		return "/" + NotificationGuiConstants.NAME;
	}

	@Override
	public boolean isVisible(final SessionIdentifier sessionIdentifier) {
		try {
			return authenticationService.isLoggedIn(sessionIdentifier);
		} catch (final AuthenticationServiceException e) {
			return false;
		}
	}

}
