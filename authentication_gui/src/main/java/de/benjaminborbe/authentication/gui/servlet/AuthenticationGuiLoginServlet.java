package de.benjaminborbe.authentication.gui.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputPasswordWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;

@Singleton
public class AuthenticationGuiLoginServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Authentication - Login";

	private static final String PARAMETER_PASSWORD = "password";

	private static final String PARAMETER_USERNAME = "username";

	private final AuthenticationService authenticationService;

	@Inject
	public AuthenticationGuiLoginServlet(
			final Logger logger,
			final CssResourceRenderer cssResourceRenderer,
			final JavascriptResourceRenderer javascriptResourceRenderer,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final AuthenticationService authenticationService) {
		super(logger, cssResourceRenderer, javascriptResourceRenderer, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, httpContextProvider);
		this.authenticationService = authenticationService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected void printContent(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		logger.trace("printContent");
		out.println("<h1>" + getTitle() + "</h1>");

		if (request.getParameter(PARAMETER_USERNAME) != null && request.getParameter(PARAMETER_PASSWORD) != null) {
			out.println("login => " + authenticationService.verifyCredential(request.getParameter(PARAMETER_USERNAME), request.getParameter(PARAMETER_PASSWORD)));
		}
		final String action = request.getContextPath() + "/login";
		final FormWidget form = new FormWidget(action);
		form.addFormInputWidget(new FormInputTextWidget(PARAMETER_USERNAME));
		form.addFormInputWidget(new FormInputPasswordWidget(PARAMETER_PASSWORD));
		form.addFormInputWidget(new FormInputSubmitWidget("login"));
		form.render(request, response, context);
	}
}