package de.benjaminborbe.monitoring.gui.servlet;

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
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.monitoring.api.MonitoringNode;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.api.MonitoringServiceException;
import de.benjaminborbe.monitoring.gui.util.MonitoringGuiLinkFactory;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class MonitoringGuiNodeListServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 3764801991353392201L;

	private static final String TITLE = "Monitoring - Node - Create";

	private final MonitoringService monitoringService;

	private final AuthenticationService authenticationService;

	private final Logger logger;

	private final MonitoringGuiLinkFactory monitoringGuiLinkFactory;

	@Inject
	public MonitoringGuiNodeListServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final Provider<HttpContext> httpContextProvider,
			final MonitoringService monitoringService,
			final UrlUtil urlUtil,
			final MonitoringGuiLinkFactory monitoringGuiLinkFactory) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.authenticationService = authenticationService;
		this.monitoringService = monitoringService;
		this.logger = logger;
		this.monitoringGuiLinkFactory = monitoringGuiLinkFactory;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final Collection<MonitoringNode> nodes = monitoringService.listNodes(sessionIdentifier);

			final UlWidget ul = new UlWidget();
			for (final MonitoringNode node : nodes) {
				final ListWidget row = new ListWidget();
				row.add(node.getName());
				row.add(" ");
				row.add(monitoringGuiLinkFactory.nodeUpdate(request, node.getId()));
				row.add(" ");
				row.add(monitoringGuiLinkFactory.nodeDelete(request, node.getId()));
				ul.add(row);
			}
			widgets.add(ul);

			final ListWidget links = new ListWidget();
			links.add(monitoringGuiLinkFactory.createNode(request));
			widgets.add(links);

			return widgets;
		}
		catch (final MonitoringServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			return exceptionWidget;
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			return exceptionWidget;
		}
	}

}
