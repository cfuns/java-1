package de.benjaminborbe.dhl.gui.service;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.dhl.api.DhlService;
import de.benjaminborbe.dhl.gui.DhlGuiConstants;
import de.benjaminborbe.navigation.api.NavigationEntry;

import javax.inject.Inject;

public class DhlGuiNavigationEntry implements NavigationEntry {

	private final AuthorizationService authorizationService;

	@Inject
	public DhlGuiNavigationEntry(final AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@Override
	public String getTitle() {
		return "Dhl";
	}

	@Override
	public String getURL() {
		return "/" + DhlGuiConstants.NAME;
	}

	@Override
	public boolean isVisible(final SessionIdentifier sessionIdentifier) {
		try {
			return authorizationService.hasPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(DhlService.PERMISSION));
		} catch (final AuthorizationServiceException e) {
			return false;
		}
	}

}
