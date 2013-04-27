package de.benjaminborbe.website.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.json.JSONObject;
import de.benjaminborbe.tools.json.JSONObjectSimple;
import de.benjaminborbe.tools.url.UrlUtil;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class WebsiteJsonServlet extends WebsiteServlet {

	private static final long serialVersionUID = -2946094661494933756L;

	public WebsiteJsonServlet(
		final Logger logger,
		final UrlUtil urlUtil,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final Provider<HttpContext> httpContextProvider
	) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
	}

	@Override
	protected void onDisabled(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		printError(response, "disabled");
	}

	@Override
	protected void onLoginRequired(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		printError(response, "loginRequired");
	}

	protected void printException(final HttpServletResponse response, final Exception exception) throws IOException {
		final JSONObject object = new JSONObjectSimple();
		object.put("error", "exception");
		object.put("type", exception.getClass().getName());
		final StringWriter sw = new StringWriter();
		exception.printStackTrace(new PrintWriter(sw));
		object.put("stack", sw.toString());
		printJson(response, object);
	}

	@Override
	protected void onPermissionDenied(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		printError(response, "permissionDenied");
	}

	protected void printError(final HttpServletResponse response, final String msg) throws IOException {
		final JSONObject object = new JSONObjectSimple();
		object.put("error", msg);
		printJson(response, object);
	}

	protected void printJson(final HttpServletResponse response, final JSONObject object) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		object.writeJSONString(response.getWriter());
	}
}
