package de.benjaminborbe.projectile.gui.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.projectile.api.ProjectileService;
import de.benjaminborbe.projectile.api.ProjectileServiceException;
import de.benjaminborbe.projectile.api.ProjectileSlacktimeReport;
import de.benjaminborbe.projectile.gui.ProjectileGuiConstants;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteJsonServlet;

public class ProjectileGuiSlacktimeReportServlet extends WebsiteJsonServlet {

	private static final long serialVersionUID = 8865908885832843737L;

	private final ProjectileService projectileService;

	private final Logger logger;

	@Inject
	public ProjectileGuiSlacktimeReportServlet(
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
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException, LoginRequiredException {

		final String token = request.getParameter(ProjectileGuiConstants.PARAMETER_AUTH_TOKEN);
		try {
			projectileService.expectAuthToken(token);
		}
		catch (final ProjectileServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
	}

	@SuppressWarnings("unchecked")
	private JSONObject buildJson(final ProjectileSlacktimeReport report) throws IOException {
		final JSONObject object = new JSONObject();
		{
			object.put("username", asString(report.getUsername()));
		}
		{
			final JSONObject week = new JSONObject();
			week.put("intern", asString(report.getWeekIntern()));
			week.put("extern", asString(report.getWeekExtern()));
			object.put("week", week);
		}
		{
			final JSONObject month = new JSONObject();
			month.put("intern", asString(report.getMonthIntern()));
			month.put("extern", asString(report.getMonthExtern()));
			object.put("month", month);
		}
		{
			final JSONObject year = new JSONObject();
			year.put("intern", asString(report.getYearIntern()));
			year.put("extern", asString(report.getYearExtern()));
			object.put("year", year);
		}
		return object;
	}

	private String asString(final Object object) {
		return object != null ? String.valueOf(object) : "";
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException, LoginRequiredException {
		try {
			logger.debug("doService");
			final String token = request.getParameter(ProjectileGuiConstants.PARAMETER_AUTH_TOKEN);
			final String username = request.getParameter(ProjectileGuiConstants.PARAMETER_USERNAME);
			final ProjectileSlacktimeReport report = projectileService.getSlacktimeReport(token, username);
			if (report != null) {
				final JSONObject jsonObject = buildJson(report);
				printJson(response, jsonObject);
			}
			else {
				printError(response, "no data found for user " + username);
			}
		}
		catch (final ProjectileServiceException e) {
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
