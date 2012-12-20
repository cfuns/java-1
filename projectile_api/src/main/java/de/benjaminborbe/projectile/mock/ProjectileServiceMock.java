package de.benjaminborbe.projectile.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.projectile.api.ProjectileService;
import de.benjaminborbe.projectile.api.ProjectileServiceException;
import de.benjaminborbe.projectile.api.ProjectileSlacktimeReport;
import de.benjaminborbe.projectile.api.ProjectileSlacktimeReportInterval;

@Singleton
public class ProjectileServiceMock implements ProjectileService {

	@Inject
	public ProjectileServiceMock() {
	}

	@Override
	public boolean validateAuthToken(final String token) throws ProjectileServiceException {
		return false;
	}

	@Override
	public void expectAuthToken(final String token) throws ProjectileServiceException, PermissionDeniedException {
	}

	@Override
	public void importReport(final SessionIdentifier sessionIdentifier, final String content, final ProjectileSlacktimeReportInterval interval) throws ProjectileServiceException,
			PermissionDeniedException, LoginRequiredException, ValidationException {
	}

	@Override
	public ProjectileSlacktimeReport getSlacktimeReport(final SessionIdentifier sessionIdentifier) throws ProjectileServiceException, PermissionDeniedException,
			LoginRequiredException {
		return null;
	}

	@Override
	public ProjectileSlacktimeReport getSlacktimeReport(final String token, final UserIdentifier userIdentifier) throws ProjectileServiceException, PermissionDeniedException {
		return null;
	}

	@Override
	public void fetchMailReport(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, ProjectileServiceException, LoginRequiredException {
	}

}
