package de.benjaminborbe.dashboard.gui.util;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.dashboard.api.DashboardService;
import de.benjaminborbe.dashboard.gui.DashboardGuiConstants;
import de.benjaminborbe.navigation.api.NavigationEntry;

import javax.inject.Inject;

public class DashboardGuiNavigationEntry implements NavigationEntry {

	private final AuthorizationService authorizationService;

	@Inject
	public DashboardGuiNavigationEntry(final AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@Override
	public String getTitle() {
		return "Dashboard";
	}

	@Override
	public String getURL() {
		return "/" + DashboardGuiConstants.NAME;
	}

	@Override
	public boolean isVisible(final SessionIdentifier sessionIdentifier) {
		try {
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(DashboardService.PERMISSION);
			return authorizationService.hasPermission(sessionIdentifier, permissionIdentifier);
		} catch (final AuthorizationServiceException e) {
			return false;
		}
	}

}
