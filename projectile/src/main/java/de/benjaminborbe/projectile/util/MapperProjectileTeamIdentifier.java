package de.benjaminborbe.projectile.util;

import de.benjaminborbe.projectile.api.ProjectileTeamIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperProjectileTeamIdentifier implements Mapper<ProjectileTeamIdentifier> {

	@Override
	public String toString(final ProjectileTeamIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public ProjectileTeamIdentifier fromString(final String value) {
		return value != null ? new ProjectileTeamIdentifier(value) : null;
	}

}
