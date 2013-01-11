package de.benjaminborbe.projectile.api;

import java.util.Collection;

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

	ProjectileSlacktimeReport getSlacktimeReportCurrentUser(SessionIdentifier sessionIdentifier) throws ProjectileServiceException, PermissionDeniedException, LoginRequiredException;

	ProjectileSlacktimeReport getSlacktimeReportForUser(String token, UserIdentifier userIdentifier) throws ProjectileServiceException, PermissionDeniedException;

	void fetchMailReport(SessionIdentifier sessionIdentifier) throws PermissionDeniedException, ProjectileServiceException, LoginRequiredException;

	Collection<ProjectileSlacktimeReport> getSlacktimeReportAllUsers(SessionIdentifier sessionIdentifier) throws PermissionDeniedException, ProjectileServiceException,
			LoginRequiredException;

	Collection<ProjectileSlacktimeReport> getSlacktimeReportCurrentTeam(SessionIdentifier sessionIdentifier) throws PermissionDeniedException, ProjectileServiceException,
			LoginRequiredException;

	Collection<ProjectileSlacktimeReport> getSlacktimeReportAllTeams(SessionIdentifier sessionIdentifier) throws PermissionDeniedException, ProjectileServiceException,
			LoginRequiredException;

	void deleteTeam(SessionIdentifier sessionIdentifier, TeamIdentifier id) throws ProjectileServiceException, PermissionDeniedException;

	void updateTeam(SessionIdentifier sessionIdentifier, TeamDto teamDto) throws ProjectileServiceException, PermissionDeniedException;

	TeamIdentifier createTeam(SessionIdentifier sessionIdentifier, TeamDto teamDto) throws ProjectileServiceException, PermissionDeniedException;

	TeamIdentifier getCurrentTeam(SessionIdentifier sessionIdentifier) throws ProjectileServiceException, PermissionDeniedException;

	Collection<TeamIdentifier> listTeams(SessionIdentifier sessionIdentifier) throws ProjectileServiceException, PermissionDeniedException;
}
