package de.benjaminborbe.selenium.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.selenium.api.SeleniumService;
import de.benjaminborbe.selenium.api.SeleniumServiceException;
import de.benjaminborbe.selenium.configuration.xml.api.SeleniumConfigurationXmlService;
import de.benjaminborbe.selenium.configuration.xml.api.SeleniumConfigurationXmlServiceException;
import de.benjaminborbe.selenium.gui.SeleniumGuiConstants;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteServlet;
import de.benjaminborbe.website.util.RedirectWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class SeleniumGuiConfigurationXmlDeleteServlet extends WebsiteServlet {

	private static final long serialVersionUID = -2358849613177992430L;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final SeleniumService seleniumService;

	private final SeleniumConfigurationXmlService seleniumConfigurationXmlService;

	@Inject
	public SeleniumGuiConfigurationXmlDeleteServlet(
		final Logger logger,
		final UrlUtil urlUtil,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final Provider<HttpContext> httpContextProvider,
		final SeleniumService seleniumService, final SeleniumConfigurationXmlService seleniumConfigurationXmlService
	) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.seleniumService = seleniumService;
		this.seleniumConfigurationXmlService = seleniumConfigurationXmlService;
	}

	@Override
	protected void doService(
		final HttpServletRequest request, final HttpServletResponse response, final HttpContext context
	) throws ServletException, IOException, PermissionDeniedException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final SeleniumConfigurationIdentifier seleniumConfigurationIdentifier = seleniumService.createSeleniumConfigurationIdentifier(request
				.getParameter(SeleniumGuiConstants.PARAMETER_CONFIGURATION_ID));
			seleniumConfigurationXmlService.deleteXml(sessionIdentifier, seleniumConfigurationIdentifier);
		} catch (final SeleniumConfigurationXmlServiceException | AuthenticationServiceException | SeleniumServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
		final RedirectWidget widget = new RedirectWidget(buildRefererUrl(request));
		widget.render(request, response, context);
	}
}
