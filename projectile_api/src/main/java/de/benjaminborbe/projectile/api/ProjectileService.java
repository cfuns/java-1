package de.benjaminborbe.projectile.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface ProjectileService {

	boolean validateAuthToken(String token) throws ProjectileServiceException;

	ProjectileSlacktimeReport getSlacktimeReport(String token, String username) throws ProjectileServiceException, PermissionDeniedException;

	void expectAuthToken(String token) throws ProjectileServiceException, PermissionDeniedException;

	void importReport(SessionIdentifier sessionIdentifier, String content, ProjectileSlacktimeReportInterval interval) throws ProjectileServiceException, PermissionDeniedException,
			LoginRequiredException, ValidationException;
}
