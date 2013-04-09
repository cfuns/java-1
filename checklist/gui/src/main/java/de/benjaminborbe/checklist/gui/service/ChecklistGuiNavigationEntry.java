package de.benjaminborbe.checklist.gui.service;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.checklist.api.ChecklistService;
import de.benjaminborbe.checklist.gui.ChecklistGuiConstants;
import de.benjaminborbe.navigation.api.NavigationEntry;

public class ChecklistGuiNavigationEntry implements NavigationEntry {

	private final AuthorizationService authorizationService;

	@Inject
	public ChecklistGuiNavigationEntry(final AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@Override
	public String getTitle() {
		return "Checklist";
	}

	@Override
	public String getURL() {
		return "/" + ChecklistGuiConstants.NAME;
	}

	@Override
	public boolean isVisible(final SessionIdentifier sessionIdentifier) {
		try {
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(ChecklistService.PERMISSION);
			return authorizationService.hasPermission(sessionIdentifier, permissionIdentifier);
		}
		catch (final AuthorizationServiceException e) {
			return false;
		}
	}

}
