package de.benjaminborbe.projectile.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.projectile.api.ProjectileService;

@Singleton
public class ProjectileServiceMock implements ProjectileService {

	@Inject
	public ProjectileServiceMock() {
	}

	@Override
	public void execute() {
	}
}
