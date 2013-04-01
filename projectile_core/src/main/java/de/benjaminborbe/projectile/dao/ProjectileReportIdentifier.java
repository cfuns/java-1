package de.benjaminborbe.projectile.dao;

import de.benjaminborbe.api.IdentifierBase;

public class ProjectileReportIdentifier extends IdentifierBase<String> {

	public ProjectileReportIdentifier(final long id) {
		this(String.valueOf(id));
	}

	public ProjectileReportIdentifier(final String id) {
		super(id);
	}

}
