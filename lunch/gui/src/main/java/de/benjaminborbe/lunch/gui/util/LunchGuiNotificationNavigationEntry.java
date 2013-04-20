package de.benjaminborbe.lunch.gui.util;

import javax.inject.Inject;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.lunch.api.LunchService;
import de.benjaminborbe.lunch.gui.LunchGuiConstants;
import de.benjaminborbe.navigation.api.NavigationEntry;

public class LunchGuiNotificationNavigationEntry implements NavigationEntry {

	private final AuthorizationService authorizationService;

	@Inject
	public LunchGuiNotificationNavigationEntry(final AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
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
			final PermissionIdentifier roleIdentifier = authorizationService.createPermissionIdentifier(LunchService.PERMISSION_NOTIFICATION);
			return authorizationService.hasPermission(sessionIdentifier, roleIdentifier);
		}
		catch (final AuthorizationServiceException e) {
			return false;
		}
	}

}
