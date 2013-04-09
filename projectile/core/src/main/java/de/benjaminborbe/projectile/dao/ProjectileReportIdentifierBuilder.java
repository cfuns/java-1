package de.benjaminborbe.projectile.dao;

import de.benjaminborbe.api.IdentifierBuilder;

public class ProjectileReportIdentifierBuilder implements IdentifierBuilder<String, ProjectileReportIdentifier> {

	@Override
	public ProjectileReportIdentifier buildIdentifier(final String value) {
		return new ProjectileReportIdentifier(value);
	}

}
