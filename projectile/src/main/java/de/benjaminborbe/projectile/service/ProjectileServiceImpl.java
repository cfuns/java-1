package de.benjaminborbe.projectile.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.projectile.api.ProjectileService;

@Singleton
public class ProjectileServiceImpl implements ProjectileService {

	private final Logger logger;

	@Inject
	public ProjectileServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.trace("execute");
	}

}
