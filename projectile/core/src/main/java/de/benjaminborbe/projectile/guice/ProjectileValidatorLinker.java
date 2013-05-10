package de.benjaminborbe.projectile.guice;

import de.benjaminborbe.lib.validation.ValidatorRegistry;
import de.benjaminborbe.projectile.dao.ProjectileTeamValidator;

import javax.inject.Inject;

public class ProjectileValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final ProjectileTeamValidator projectileTeamValidator) {
		validatorRegistry.register(projectileTeamValidator);
	}
}
