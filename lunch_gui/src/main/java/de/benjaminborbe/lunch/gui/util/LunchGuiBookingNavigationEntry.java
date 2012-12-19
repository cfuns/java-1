package de.benjaminborbe.lunch.gui.util;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.lunch.gui.LunchGuiConstants;
import de.benjaminborbe.navigation.api.NavigationEntry;

public class LunchGuiBookingNavigationEntry implements NavigationEntry {

	private final AuthorizationService authorizationService;

	@Inject
	public LunchGuiBookingNavigationEntry(final AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@Override
	public String getTitle() {
		return "Booking";
	}

	@Override
	public String getURL() {
		return "/" + LunchGuiConstants.NAME + LunchGuiConstants.URL_BOOKING;
	}

	@Override
	public boolean isVisible(final SessionIdentifier sessionIdentifier) {
		try {
			final RoleIdentifier roleIdentifier = authorizationService.createRoleIdentifier(LunchGuiConstants.LUNCH_ADMIN_ROLENAME);
			return authorizationService.hasRole(sessionIdentifier, roleIdentifier);
		}
		catch (final AuthorizationServiceException e) {
			return false;
		}
	}

}
