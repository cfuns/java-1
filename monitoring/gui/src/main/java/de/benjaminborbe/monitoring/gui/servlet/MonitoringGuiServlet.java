package de.benjaminborbe.monitoring.gui.servlet;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.api.MonitoringServiceException;
import de.benjaminborbe.monitoring.gui.MonitoringGuiConstants;
import de.benjaminborbe.monitoring.gui.util.MonitoringGuiLinkFactory;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.CssResourceImpl;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Singleton
public class MonitoringGuiServlet extends MonitoringWebsiteHtmlServlet {

	private static final long serialVersionUID = -2191141253201553995L;

	private static final String TITLE = "Monitoring";

	private final Logger logger;

	private final MonitoringGuiLinkFactory monitoringGuiLinkFactory;

	private final MonitoringService monitoringService;

	private final AuthenticationService authenticationService;

	@Inject
	public MonitoringGuiServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final MonitoringGuiLinkFactory monitoringGuiLinkFactory,
		final MonitoringService monitoringService,
		final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.monitoringGuiLinkFactory = monitoringGuiLinkFactory;
		this.monitoringService = monitoringService;
		this.authenticationService = authenticationService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createMonitoringContentWidget(final HttpServletRequest request) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));
			final UlWidget ul = new UlWidget();
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			if (monitoringService.hasMonitoringViewOrAdminPermission(sessionIdentifier)) {
				ul.add(monitoringGuiLinkFactory.nodeList(request));
			}
			if (monitoringService.hasMonitoringAdminPermission(sessionIdentifier)) {
				ul.add(monitoringGuiLinkFactory.check(request));
				ul.add(monitoringGuiLinkFactory.mail(request));
			}
			widgets.add(ul);
			return widgets;
		} catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			return exceptionWidget;
		} catch (final MonitoringServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			return exceptionWidget;
		}
	}

	@Override
	protected Collection<CssResource> getCssResources(final HttpServletRequest request, final HttpServletResponse response) {
		final Collection<CssResource> result = super.getCssResources(request, response);
		result.add(new CssResourceImpl(request.getContextPath() + "/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_CSS_STYLE));
		return result;
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request) throws ServletException, IOException,
		PermissionDeniedException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			monitoringService.expectMonitoringViewOrAdminPermission(sessionIdentifier);
		} catch (final AuthenticationServiceException | MonitoringServiceException e) {
			throw new PermissionDeniedException(e);
		}
	}

}
