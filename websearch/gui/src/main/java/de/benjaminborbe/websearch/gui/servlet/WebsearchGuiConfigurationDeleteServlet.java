package de.benjaminborbe.websearch.gui.servlet;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.websearch.api.WebsearchConfigurationIdentifier;
import de.benjaminborbe.websearch.api.WebsearchService;
import de.benjaminborbe.websearch.api.WebsearchServiceException;
import de.benjaminborbe.websearch.gui.WebsearchGuiConstants;
import de.benjaminborbe.website.servlet.WebsiteServlet;
import de.benjaminborbe.website.util.RedirectWidget;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class WebsearchGuiConfigurationDeleteServlet extends WebsiteServlet {

	private static final long serialVersionUID = 7727468974460815201L;

	private final WebsearchService websearchService;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	@Inject
	public WebsearchGuiConfigurationDeleteServlet(
		final Logger logger,
		final UrlUtil urlUtil,
		final AuthenticationService authenticationService,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final Provider<HttpContext> httpContextProvider,
		final WebsearchService websearchService,
		final AuthorizationService authorizationService) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.websearchService = websearchService;
		this.logger = logger;
		this.authenticationService = authenticationService;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
		PermissionDeniedException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final WebsearchConfigurationIdentifier websearchConfigurationIdentifier = websearchService.createConfigurationIdentifier(request
				.getParameter(WebsearchGuiConstants.PARAMETER_CONFIGURATION_ID));
			websearchService.deleteConfiguration(sessionIdentifier, websearchConfigurationIdentifier);
		} catch (final WebsearchServiceException | LoginRequiredException | AuthenticationServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
		final RedirectWidget widget = new RedirectWidget(buildRefererUrl(request));
		widget.render(request, response, context);
	}
}
