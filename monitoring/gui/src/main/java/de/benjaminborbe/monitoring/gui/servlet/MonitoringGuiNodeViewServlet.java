package de.benjaminborbe.monitoring.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.monitoring.api.MonitoringNode;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.api.MonitoringServiceException;
import de.benjaminborbe.monitoring.gui.MonitoringGuiConstants;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.PreWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Singleton
public class MonitoringGuiNodeViewServlet extends MonitoringWebsiteHtmlServlet {

	private static final long serialVersionUID = -7050670476472197641L;

	private static final String TITLE = "Monitoring - Node View";

	private final AuthenticationService authenticationService;

	private final MonitoringService monitoringService;

	@Inject
	public MonitoringGuiNodeViewServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final CacheService cacheService,
		final MonitoringService monitoringService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.authenticationService = authenticationService;
		this.monitoringService = monitoringService;
	}

	@Override
	protected Widget createMonitoringContentWidget(final HttpServletRequest request) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final MonitoringNodeIdentifier monitoringNodeIdentifier = monitoringService.createNodeIdentifier(request.getParameter(MonitoringGuiConstants.PARAMETER_NODE_ID));
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final MonitoringNode node = monitoringService.getNode(sessionIdentifier, monitoringNodeIdentifier);

			final ListWidget name = new ListWidget();
			name.add(node.getDescription());
			name.add(" (");
			name.add(node.getName());
			name.add(") ");

			widgets.add(new H2Widget(name));
			widgets.add(node.getMessage());
			widgets.add(new PreWidget(node.getException()));

			return widgets;
		} catch (final MonitoringServiceException | AuthenticationServiceException e) {
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			return exceptionWidget;
		}
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

}
