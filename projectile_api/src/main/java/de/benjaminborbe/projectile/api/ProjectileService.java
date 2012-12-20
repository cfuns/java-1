package de.benjaminborbe.projectile.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface ProjectileService {

	boolean validateAuthToken(String token) throws ProjectileServiceException;

	void expectAuthToken(String token) throws ProjectileServiceException, PermissionDeniedException;

	void importReport(SessionIdentifier sessionIdentifier, String content, ProjectileSlacktimeReportInterval interval) throws ProjectileServiceException, PermissionDeniedException,
			LoginRequiredException, ValidationException;

	ProjectileSlacktimeReport getSlacktimeReport(SessionIdentifier sessionIdentifier) throws ProjectileServiceException, PermissionDeniedException, LoginRequiredException;

	ProjectileSlacktimeReport getSlacktimeReport(String token, UserIdentifier userIdentifier) throws ProjectileServiceException, PermissionDeniedException;

	void fetchMailReport(SessionIdentifier sessionIdentifier) throws PermissionDeniedException, ProjectileServiceException, LoginRequiredException;
}
