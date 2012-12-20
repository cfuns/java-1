package de.benjaminborbe.projectile.api;

import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface ProjectileService {

	boolean validateAuthToken(String token) throws ProjectileServiceException;

	ProjectileSlacktimeReport getSlacktimeReport(String token, String username) throws ProjectileServiceException, PermissionDeniedException;

	void expectAuthToken(String token) throws ProjectileServiceException, PermissionDeniedException;
}
