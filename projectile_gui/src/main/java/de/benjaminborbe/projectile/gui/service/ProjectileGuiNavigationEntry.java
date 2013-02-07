package de.benjaminborbe.projectile.gui.service;

import com.google.inject.Inject;

import de.benjaminborbe.projectile.api.ProjectileService;
import de.benjaminborbe.projectile.api.ProjectileServiceException;
import de.benjaminborbe.projectile.gui.ProjectileGuiConstants;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.navigation.api.NavigationEntry;

public class ProjectileGuiNavigationEntry implements NavigationEntry {

	private final ProjectileService projectileService;

	@Inject
	public ProjectileGuiNavigationEntry(final ProjectileService projectileService) {
		this.projectileService = projectileService;
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
			return projectileService.hasProjectileAdminRole(sessionIdentifier);
		}
		catch (final ProjectileServiceException e) {
			return false;
		}
	}

}
