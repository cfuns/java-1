package de.benjaminborbe.projectile.gui.service;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.projectile.gui.ProjectileGuiConstants;

public class ProjectileGuiNavigationEntry implements NavigationEntry {

	private final AuthenticationService authenticationService;

	@Inject
	public ProjectileGuiNavigationEntry(final AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@Override
	public String getTitle() {
		return "Projectile";
	}

	@Override
	public String getURL() {
		return "/" + ProjectileGuiConstants.NAME;
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
