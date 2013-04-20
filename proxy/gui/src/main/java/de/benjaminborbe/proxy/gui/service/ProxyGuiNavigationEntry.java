package de.benjaminborbe.proxy.gui.service;

import javax.inject.Inject;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.proxy.gui.ProxyGuiConstants;

public class ProxyGuiNavigationEntry implements NavigationEntry {

	private final AuthorizationService authorizationService;

	@Inject
	public ProxyGuiNavigationEntry(final AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@Override
	public String getTitle() {
		return "Proxy";
	}

	@Override
	public String getURL() {
		return "/" + ProxyGuiConstants.NAME + ProxyGuiConstants.URL_ADMIN;
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
