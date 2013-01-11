package de.benjaminborbe.projectile.api;

import de.benjaminborbe.api.IdentifierBase;

public class ProjectileTeamIdentifier extends IdentifierBase<String> {

	public ProjectileTeamIdentifier(final long id) {
		this(String.valueOf(id));
	}

	public ProjectileTeamIdentifier(final String id) {
		super(id);
	}

}
