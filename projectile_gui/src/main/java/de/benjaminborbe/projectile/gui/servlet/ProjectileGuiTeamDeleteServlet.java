package de.benjaminborbe.projectile.gui.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.projectile.api.ProjectileService;
import de.benjaminborbe.projectile.api.ProjectileServiceException;
import de.benjaminborbe.projectile.api.ProjectileTeamIdentifier;
import de.benjaminborbe.projectile.gui.ProjectileGuiConstants;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteServlet;
import de.benjaminborbe.website.util.RedirectWidget;

@Singleton
public class ProjectileGuiTeamDeleteServlet extends WebsiteServlet {

	private static final long serialVersionUID = -7862318070826148848L;

	private final ProjectileService projectileService;

	private final AuthenticationService authenticationService;

	private final Logger logger;

	private final AuthorizationService authorizationService;

	@Inject
	public ProjectileGuiTeamDeleteServlet(
			final Logger logger,
			final UrlUtil urlUtil,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider,
			final ProjectileService projectileService) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.projectileService = projectileService;
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final ProjectileTeamIdentifier projectileTeamIdentifier = new ProjectileTeamIdentifier(request.getParameter(ProjectileGuiConstants.PARAMETER_TEAM_ID));
			projectileService.deleteTeam(sessionIdentifier, projectileTeamIdentifier);
		}
		catch (final AuthenticationServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
		catch (final ProjectileServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
		final RedirectWidget widget = new RedirectWidget(buildRefererUrl(request));
		widget.render(request, response, context);
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final RoleIdentifier roleIdentifier = authorizationService.createRoleIdentifier(ProjectileService.PROJECTILE_ADMIN_ROLENAME);
			authorizationService.expectRole(sessionIdentifier, roleIdentifier);
		}
		catch (final AuthenticationServiceException e) {
			throw new PermissionDeniedException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new PermissionDeniedException(e);
		}
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

}
