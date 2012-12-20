package de.benjaminborbe.projectile.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.projectile.api.ProjectileService;
import de.benjaminborbe.projectile.api.ProjectileServiceException;
import de.benjaminborbe.projectile.api.ProjectileSlacktimeReport;
import de.benjaminborbe.projectile.config.ProjectileConfig;

@Singleton
public class ProjectileServiceImpl implements ProjectileService {

	private final Logger logger;

	private final ProjectileConfig projectileConfig;

	@Inject
	public ProjectileServiceImpl(final Logger logger, final ProjectileConfig projectileConfig) {
		this.logger = logger;
		this.projectileConfig = projectileConfig;
	}

	@Override
	public boolean validateAuthToken(final String token) throws ProjectileServiceException {
		logger.debug("validateAuthToken");
		final String authToken = projectileConfig.getAuthToken();
		return authToken != null && token != null && authToken.equals(token);
	}

	@Override
	public void expectAuthToken(final String token) throws ProjectileServiceException, PermissionDeniedException {
		if (!validateAuthToken(token)) {
			throw new PermissionDeniedException("token invalid");
		}
	}

	@Override
	public ProjectileSlacktimeReport getSlacktimeReport(final String token, final String username) throws ProjectileServiceException, PermissionDeniedException {
		expectAuthToken(token);
		logger.debug("getSlacktimeReport");
		return null;
	}

}
