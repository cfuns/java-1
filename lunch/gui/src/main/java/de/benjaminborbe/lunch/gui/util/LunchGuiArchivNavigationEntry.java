package de.benjaminborbe.lunch.gui.util;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.lunch.api.LunchService;
import de.benjaminborbe.lunch.gui.LunchGuiConstants;
import de.benjaminborbe.navigation.api.NavigationEntry;

public class LunchGuiArchivNavigationEntry implements NavigationEntry {

	private final AuthorizationService authorizationService;

	@Inject
	public LunchGuiArchivNavigationEntry(final AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@Override
	public String getTitle() {
		return "Mittagessen-Archiv";
	}

	@Override
	public String getURL() {
		return "/" + LunchGuiConstants.NAME + LunchGuiConstants.URL_ARCHIV;
	}

	@Override
	public boolean isVisible(final SessionIdentifier sessionIdentifier) {
		try {
			final PermissionIdentifier roleIdentifier = authorizationService.createPermissionIdentifier(LunchService.PERMISSION_VIEW_ARCHIVE);
			return authorizationService.hasPermission(sessionIdentifier, roleIdentifier);
		}
		catch (final AuthorizationServiceException e) {
			return false;
		}
	}

}
