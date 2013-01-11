package de.benjaminborbe.projectile.gui.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.common.collect.Lists;
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
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.projectile.api.ProjectileService;
import de.benjaminborbe.projectile.api.ProjectileServiceException;
import de.benjaminborbe.projectile.api.ProjectileTeam;
import de.benjaminborbe.projectile.gui.util.ProjectileLinkFactory;
import de.benjaminborbe.projectile.gui.util.TeamComparator;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class ProjectileGuiTeamListServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = -1955828859355943385L;

	private static final String TITLE = "Projectile - Team - List";

	private final AuthenticationService authenticationService;

	private final ProjectileService projectileService;

	private final ProjectileLinkFactory projectileLinkFactory;

	private final Logger logger;

	private final AuthorizationService authorizationService;

	@Inject
	public ProjectileGuiTeamListServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final Provider<HttpContext> httpContextProvider,
			final UrlUtil urlUtil,
			final ProjectileService projectileService,
			final ProjectileLinkFactory projectileLinkFactory) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
		this.projectileService = projectileService;
		this.logger = logger;
		this.projectileLinkFactory = projectileLinkFactory;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(TITLE));
			final UlWidget ul = new UlWidget();
			final List<ProjectileTeam> teams = Lists.newArrayList(projectileService.listTeams(sessionIdentifier));
			Collections.sort(teams, new TeamComparator());
			for (final ProjectileTeam team : teams) {
				final ListWidget list = new ListWidget();
				list.add(projectileLinkFactory.viewTeam(request, team.getId(), team.getName()));
				list.add(" ");
				list.add(projectileLinkFactory.updateTeam(request, team.getId()));
				list.add(" ");
				list.add(projectileLinkFactory.deleteTeam(request, team.getId()));
				ul.add(list);
			}
			widgets.add(ul);
			widgets.add(projectileLinkFactory.createTeam(request));
			return widgets;
		}
		catch (final ProjectileServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		}
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
