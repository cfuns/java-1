package de.benjaminborbe.projectile.mock;

import java.util.Collection;

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
import de.benjaminborbe.projectile.api.ProjectileTeam;
import de.benjaminborbe.projectile.api.ProjectileTeamDto;
import de.benjaminborbe.projectile.api.ProjectileTeamIdentifier;

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
	public ProjectileSlacktimeReport getSlacktimeReportCurrentUser(final SessionIdentifier sessionIdentifier) throws ProjectileServiceException, PermissionDeniedException,
			LoginRequiredException {
		return null;
	}

	@Override
	public ProjectileSlacktimeReport getSlacktimeReportForUser(final String token, final UserIdentifier userIdentifier) throws ProjectileServiceException, PermissionDeniedException {
		return null;
	}

	@Override
	public void fetchMailReport(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, ProjectileServiceException, LoginRequiredException {
	}

	@Override
	public Collection<ProjectileSlacktimeReport> getSlacktimeReportAllUsers(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, ProjectileServiceException,
			LoginRequiredException {
		return null;
	}

	@Override
	public Collection<ProjectileSlacktimeReport> getSlacktimeReportCurrentTeam(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException,
			ProjectileServiceException, LoginRequiredException {
		return null;
	}

	@Override
	public Collection<ProjectileSlacktimeReport> getSlacktimeReportAllTeams(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, ProjectileServiceException,
			LoginRequiredException {
		return null;
	}

	@Override
	public void deleteTeam(final SessionIdentifier sessionIdentifier, final ProjectileTeamIdentifier id) throws ProjectileServiceException, PermissionDeniedException {
	}

	@Override
	public void updateTeam(final SessionIdentifier sessionIdentifier, final ProjectileTeamDto teamDto) throws ProjectileServiceException, PermissionDeniedException {
	}

	@Override
	public ProjectileTeamIdentifier createTeam(final SessionIdentifier sessionIdentifier, final ProjectileTeamDto teamDto) throws ProjectileServiceException, PermissionDeniedException {
		return null;
	}

	@Override
	public ProjectileTeamIdentifier getCurrentTeam(final SessionIdentifier sessionIdentifier) throws ProjectileServiceException, PermissionDeniedException {
		return null;
	}

	@Override
	public Collection<ProjectileTeam> listTeams(final SessionIdentifier sessionIdentifier) throws ProjectileServiceException, PermissionDeniedException {
		return null;
	}

}
