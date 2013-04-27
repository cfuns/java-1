package de.benjaminborbe.message.gui.service;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.message.gui.MessageGuiConstants;
import de.benjaminborbe.navigation.api.NavigationEntry;

import javax.inject.Inject;

public class MessageGuiNavigationEntry implements NavigationEntry {

	private final AuthorizationService authorizationService;

	@Inject
	public MessageGuiNavigationEntry(final AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@Override
	public String getTitle() {
		return "Message";
	}

	@Override
	public String getURL() {
		return "/" + MessageGuiConstants.NAME;
	}

	@Override
	public boolean isVisible(final SessionIdentifier sessionIdentifier) {
		try {
			return authorizationService.hasAdminRole(sessionIdentifier);
		} catch (final AuthorizationServiceException e) {
			return false;
		}
	}

}
