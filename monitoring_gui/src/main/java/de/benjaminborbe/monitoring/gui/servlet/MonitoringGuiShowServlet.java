package de.benjaminborbe.monitoring.gui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
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
import de.benjaminborbe.monitoring.gui.util.MonitoringNodeComparator;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.SpanWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class MonitoringGuiShowServlet extends MonitoringWebsiteHtmlServlet {

	private static final long serialVersionUID = -7863752062438502326L;

	private static final String TITLE = "Monitoring - View";

	private final Logger logger;

	private final MonitoringService monitoringService;

	private final AuthenticationService authenticationService;

	private final MonitoringGuiLinkFactory monitoringGuiLinkFactory;

	@Inject
	public MonitoringGuiShowServlet(
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
		this.logger = logger;
		this.monitoringService = monitoringService;
		this.authenticationService = authenticationService;
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
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final List<MonitoringNode> results = new ArrayList<MonitoringNode>(monitoringService.getCheckResults(sessionIdentifier));
			Collections.sort(results, new MonitoringNodeComparator());
			final UlWidget ul = new UlWidget();
			for (final MonitoringNode result : results) {

				final ListWidget row = new ListWidget();
				row.add("[");
				if (result.getResult() == null) {
					row.add(new SpanWidget("???").addClass("checkResultUnknown"));
				}
				else if (Boolean.TRUE.equals(result.getResult())) {
					row.add(new SpanWidget("OK").addClass("checkResultOk"));
				}
				else {
					row.add(new SpanWidget("FAIL").addClass("checkResultFail"));
				}
				row.add("] ");

				row.add(result.getDescription());
				row.add(" (");
				row.add(result.getName());
				row.add(") ");
				if (Boolean.FALSE.equals(result.getResult())) {
					row.add("(");
					row.add(result.getMessage() != null ? result.getMessage() : "-");
					row.add(")");
					row.add(" ");
				}

				if (monitoringService.hasMonitoringAdminRole(sessionIdentifier)) {
					row.add(monitoringGuiLinkFactory.nodeSilent(request, result.getId()));
					row.add(" ");
					row.add(monitoringGuiLinkFactory.nodeUpdate(request, result.getId()));
					row.add(" ");
					row.add(monitoringGuiLinkFactory.nodeDelete(request, result.getId()));
				}

				ul.add(row);
			}
			widgets.add(ul);

			final ListWidget links = new ListWidget();
			if (monitoringService.hasMonitoringAdminRole(sessionIdentifier)) {
				links.add(monitoringGuiLinkFactory.createNode(request));
			}
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

	@Override
	public boolean isAdminRequired() {
		return false;
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			monitoringService.expectMonitoringViewOrAdminRole(sessionIdentifier);
		}
		catch (final AuthenticationServiceException e) {
			throw new PermissionDeniedException(e);
		}
		catch (final MonitoringServiceException e) {
			throw new PermissionDeniedException(e);
		}
	}
}
