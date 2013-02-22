package de.benjaminborbe.analytics.gui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.gui.AnalyticsGuiConstants;
import de.benjaminborbe.analytics.gui.config.AnalyticsGuiConfig;
import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.json.JSONObject;
import de.benjaminborbe.tools.json.JSONObjectSimple;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.validation.ValidationResultImpl;
import de.benjaminborbe.website.servlet.WebsiteJsonServlet;

@Singleton
public class AnalyticsGuiReportAddDataJsonServlet extends WebsiteJsonServlet {

	private static final long serialVersionUID = 1844470197045483190L;

	private final Logger logger;

	private final AnalyticsGuiConfig analyticsGuiConfig;

	private final AuthenticationService authenticationService;

	private final AnalyticsService analyticsService;

	private final ParseUtil parseUtil;

	@Inject
	public AnalyticsGuiReportAddDataJsonServlet(
			final Logger logger,
			final UrlUtil urlUtil,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider,
			final AnalyticsGuiConfig analyticsGuiConfig,
			final AnalyticsService analyticsService,
			final ParseUtil parseUtil) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.logger = logger;
		this.analyticsGuiConfig = analyticsGuiConfig;
		this.authenticationService = authenticationService;
		this.analyticsService = analyticsService;
		this.parseUtil = parseUtil;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException, LoginRequiredException {
		try {
			if (logger.isTraceEnabled())
				logger.trace("doService");
			final String token = request.getParameter(AnalyticsGuiConstants.PARAMETER_AUTH_TOKEN);
			final String reportId = request.getParameter(AnalyticsGuiConstants.PARAMETER_REPORT_ID);
			final String value = request.getParameter(AnalyticsGuiConstants.PARAMETER_VALUE);

			if (token != null && reportId != null && value != null) {
				final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

				try {
					final AnalyticsReportIdentifier analyticsReportIdentifier = new AnalyticsReportIdentifier(reportId);
					addData(sessionIdentifier, analyticsReportIdentifier, value);
					final JSONObject object = new JSONObjectSimple();
					object.put("result", "success");
					printJson(response, object);
				}
				catch (final ValidationException e) {
					logger.warn(e.getClass().getName(), e);
					printError(response, "add value failed");
				}
			}
		}
		catch (final AnalyticsServiceException e) {
			printException(response, e);
		}
		catch (final AuthenticationServiceException e) {
			printException(response, e);
		}
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException, LoginRequiredException {
		final String token = request.getParameter(AnalyticsGuiConstants.PARAMETER_AUTH_TOKEN);
		if (logger.isTraceEnabled())
			logger.trace("doCheckPermission");
		expectAuthToken(token);
	}

	private void expectAuthToken(final String token) throws PermissionDeniedException {
		if (analyticsGuiConfig.getAuthToken() == null || token == null || !analyticsGuiConfig.getAuthToken().equals(token)) {
			throw new PermissionDeniedException("invalid token");
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

	private void addData(final SessionIdentifier sessionIdentifier, final AnalyticsReportIdentifier analyticsReportIdentifier, final String valueString)
			throws AnalyticsServiceException, ValidationException, PermissionDeniedException, LoginRequiredException {
		final List<ValidationError> errors = new ArrayList<ValidationError>();

		double value = 0;
		{
			try {
				value = parseUtil.parseDouble(valueString);
			}
			catch (final ParseException e) {
				errors.add(new ValidationErrorSimple("illegal value"));
			}
		}

		if (!errors.isEmpty()) {
			throw new ValidationException(new ValidationResultImpl(errors));
		}
		else {
			analyticsService.addReportValue(analyticsReportIdentifier, value);
		}
	}
}
