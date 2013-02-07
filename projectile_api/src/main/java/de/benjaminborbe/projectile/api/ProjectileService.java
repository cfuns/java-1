package de.benjaminborbe.projectile.api;

import java.util.Collection;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface ProjectileService {

	String PROJECTILE_ADMIN_ROLENAME = "ProjectileAdmin";

	boolean validateAuthToken(String token) throws ProjectileServiceException;

	void expectAuthToken(String token) throws ProjectileServiceException, PermissionDeniedException;

	void importReport(SessionIdentifier sessionIdentifier, String content, ProjectileSlacktimeReportInterval interval) throws ProjectileServiceException, PermissionDeniedException,
			LoginRequiredException, ValidationException;

	ProjectileSlacktimeReport getSlacktimeReportCurrentUser(SessionIdentifier sessionIdentifier) throws ProjectileServiceException, PermissionDeniedException, LoginRequiredException;

	ProjectileSlacktimeReport getSlacktimeReportForUser(String token, UserIdentifier userIdentifier) throws ProjectileServiceException, PermissionDeniedException;

	void fetchMailReport(SessionIdentifier sessionIdentifier) throws PermissionDeniedException, ProjectileServiceException, LoginRequiredException;

	Collection<ProjectileSlacktimeReport> getSlacktimeReportAllUsers(SessionIdentifier sessionIdentifier) throws PermissionDeniedException, ProjectileServiceException,
			LoginRequiredException;

	ProjectileSlacktimeReport getSlacktimeReportCurrentTeam(SessionIdentifier sessionIdentifier) throws PermissionDeniedException, ProjectileServiceException, LoginRequiredException;

	Collection<ProjectileSlacktimeReport> getSlacktimeReportAllTeams(SessionIdentifier sessionIdentifier) throws PermissionDeniedException, ProjectileServiceException,
			LoginRequiredException;

	void deleteTeam(SessionIdentifier sessionIdentifier, ProjectileTeamIdentifier id) throws ProjectileServiceException, PermissionDeniedException, LoginRequiredException;

	void updateTeam(SessionIdentifier sessionIdentifier, ProjectileTeamDto teamDto) throws ProjectileServiceException, PermissionDeniedException, ValidationException,
			LoginRequiredException;

	ProjectileTeamIdentifier createTeam(SessionIdentifier sessionIdentifier, ProjectileTeamDto teamDto) throws ProjectileServiceException, PermissionDeniedException,
			ValidationException, LoginRequiredException;

	ProjectileTeamIdentifier getCurrentTeam(SessionIdentifier sessionIdentifier) throws ProjectileServiceException, PermissionDeniedException, LoginRequiredException;

	Collection<ProjectileTeam> listTeams(SessionIdentifier sessionIdentifier) throws ProjectileServiceException, PermissionDeniedException;

	ProjectileTeam getTeam(SessionIdentifier sessionIdentifier, ProjectileTeamIdentifier projectileTeamIdentifier) throws ProjectileServiceException, PermissionDeniedException,
			LoginRequiredException;

	void addUserToTeam(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier, final ProjectileTeamIdentifier projectileTeamIdentifier)
			throws ProjectileServiceException, PermissionDeniedException, LoginRequiredException;

	void removeUserFromTeam(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier, final ProjectileTeamIdentifier projectileTeamIdentifier)
			throws ProjectileServiceException, PermissionDeniedException, LoginRequiredException;

	ProjectileTeamIdentifier getTeamForUser(SessionIdentifier sessionIdentifier, UserIdentifier userIdentifier) throws ProjectileServiceException, PermissionDeniedException,
			LoginRequiredException;

	Collection<UserIdentifier> getUsersForTeam(SessionIdentifier sessionIdentifier, ProjectileTeamIdentifier projectileTeamIdentifier) throws ProjectileServiceException,
			PermissionDeniedException, LoginRequiredException;

	void expectProjectileAdminRole(SessionIdentifier sessionIdentifier) throws ProjectileServiceException, PermissionDeniedException, LoginRequiredException;

	boolean hasProjectileAdminRole(SessionIdentifier sessionIdentifier) throws ProjectileServiceException;

}
