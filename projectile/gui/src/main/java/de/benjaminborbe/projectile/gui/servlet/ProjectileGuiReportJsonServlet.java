package de.benjaminborbe.projectile.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.projectile.api.ProjectileService;
import de.benjaminborbe.projectile.api.ProjectileServiceException;
import de.benjaminborbe.projectile.api.ProjectileSlacktimeReport;
import de.benjaminborbe.projectile.gui.ProjectileGuiConstants;
import de.benjaminborbe.projectile.gui.util.ProjectileReportToJsonConverter;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.json.JSONObject;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteJsonServlet;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class ProjectileGuiReportJsonServlet extends WebsiteJsonServlet {

	private static final long serialVersionUID = 8865908885832843737L;

	private final ProjectileService projectileService;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final ProjectileReportToJsonConverter projectileReportToJsonConverter;

	@Inject
	public ProjectileGuiReportJsonServlet(
		final Logger logger,
		final UrlUtil urlUtil,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final Provider<HttpContext> httpContextProvider,
		final ProjectileService projectileService,
		final ProjectileReportToJsonConverter projectileReportToJsonConverter
	) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.projectileService = projectileService;
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.projectileReportToJsonConverter = projectileReportToJsonConverter;
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request) throws ServletException, IOException,
		PermissionDeniedException, LoginRequiredException {
		try {
			final String token = request.getParameter(ProjectileGuiConstants.PARAMETER_AUTH_TOKEN);
			logger.debug("doCheckPermission");
			projectileService.expectAuthToken(token);
		} catch (final ProjectileServiceException e) {
			throw new PermissionDeniedException(e);
		}
	}

	@Override
	protected void doService(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final HttpContext context
	) throws ServletException, IOException,
		PermissionDeniedException, LoginRequiredException {
		try {
			logger.debug("doService");
			final String token = request.getParameter(ProjectileGuiConstants.PARAMETER_AUTH_TOKEN);
			final String username = request.getParameter(ProjectileGuiConstants.PARAMETER_USERNAME);
			if (token != null && username != null) {
				final UserIdentifier userIdentifier = authenticationService.createUserIdentifier(username);
				final ProjectileSlacktimeReport report = projectileService.getSlacktimeReportForUser(token, userIdentifier);
				if (report != null) {
					final JSONObject jsonObject = projectileReportToJsonConverter.convert(report);
					printJson(response, jsonObject);
				} else {
					printError(response, "no data found for user " + username);
				}
			} else {
				printError(response, "parameter required: " + ProjectileGuiConstants.PARAMETER_AUTH_TOKEN + " and " + ProjectileGuiConstants.PARAMETER_USERNAME);
			}
		} catch (final ProjectileServiceException e) {
			printException(response, e);
		} catch (AuthenticationServiceException e) {
			printException(response, e);
		}
	}

	@Override
	public boolean isLoginRequired() {
		return false;
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

}
