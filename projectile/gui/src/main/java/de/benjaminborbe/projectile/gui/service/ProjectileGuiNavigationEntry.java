package de.benjaminborbe.projectile.gui.service;

import javax.inject.Inject;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.projectile.api.ProjectileService;
import de.benjaminborbe.projectile.gui.ProjectileGuiConstants;

public class ProjectileGuiNavigationEntry implements NavigationEntry {

	private final AuthorizationService authorizationService;

	@Inject
	public ProjectileGuiNavigationEntry(final AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
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
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(ProjectileService.PERMISSION_VIEW);
			return authorizationService.hasPermission(sessionIdentifier, permissionIdentifier);
		}
		catch (final AuthorizationServiceException e) {
			return false;
		}
	}

}
