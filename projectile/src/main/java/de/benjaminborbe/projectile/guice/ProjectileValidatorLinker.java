package de.benjaminborbe.projectile.guice;

import com.google.inject.Inject;

import de.benjaminborbe.projectile.dao.ProjectileTeamValidator;
import de.benjaminborbe.tools.validation.ValidatorRegistry;

public class ProjectileValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final ProjectileTeamValidator projectileTeamValidator) {
		validatorRegistry.register(projectileTeamValidator);
	}
}
