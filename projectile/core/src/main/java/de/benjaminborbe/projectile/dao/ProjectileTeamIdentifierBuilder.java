package de.benjaminborbe.projectile.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.projectile.api.ProjectileTeamIdentifier;

public class ProjectileTeamIdentifierBuilder implements IdentifierBuilder<String, ProjectileTeamIdentifier> {

	@Override
	public ProjectileTeamIdentifier buildIdentifier(final String value) {
		return new ProjectileTeamIdentifier(value);
	}

}
