package de.benjaminborbe.projectile.gui.servlet;

import java.io.IOException;
import java.util.Collection;

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
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.projectile.api.ProjectileService;
import de.benjaminborbe.projectile.api.ProjectileServiceException;
import de.benjaminborbe.projectile.api.ProjectileSlacktimeReport;
import de.benjaminborbe.projectile.api.ProjectileTeamIdentifier;
import de.benjaminborbe.projectile.gui.widget.ProjectileSingleReport;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class ProjectileGuiReportTeamCurrentServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = -1955828859355943385L;

	private static final String TITLE = "Projectile - Report Team";

	private final ProjectileService projectileService;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	@Inject
	public ProjectileGuiReportTeamCurrentServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final Provider<HttpContext> httpContextProvider,
			final UrlUtil urlUtil,
			final ProjectileService projectileService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.projectileService = projectileService;
		this.logger = logger;
		this.authenticationService = authenticationService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final ProjectileSlacktimeReport report = projectileService.getSlacktimeReportCurrentTeam(sessionIdentifier);
			if (report != null) {
				final ProjectileTeamIdentifier projectileTeamIdentifier = projectileService.getCurrentTeam(sessionIdentifier);
				final Collection<UserIdentifier> users = projectileService.getUsersForTeam(sessionIdentifier, projectileTeamIdentifier);

				widgets.add(new H1Widget(getTitle() + " " + report.getName()));
				widgets.add(new ProjectileSingleReport(report));

				widgets.add(new H2Widget("Users:"));
				final UlWidget ul = new UlWidget();
				for (final UserIdentifier user : users) {
					ul.add(user.getId());
				}
				widgets.add(ul);
			}
			else {
				widgets.add("no data found");
			}

			return widgets;
		}
		catch (final ProjectileServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}
}
