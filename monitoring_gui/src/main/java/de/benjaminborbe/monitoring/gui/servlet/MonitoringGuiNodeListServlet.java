package de.benjaminborbe.monitoring.gui.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
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
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.monitoring.api.MonitoringNode;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.api.MonitoringServiceException;
import de.benjaminborbe.monitoring.gui.util.MonitoringGuiLinkFactory;
import de.benjaminborbe.monitoring.tools.MonitoringNodeComparator;
import de.benjaminborbe.monitoring.tools.MonitoringNodeTree;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.SpanWidget;
import de.benjaminborbe.website.util.UlWidget;
import de.benjaminborbe.website.widget.TooltipWidget;
import de.benjaminborbe.website.widget.VoidWidget;

@Singleton
public class MonitoringGuiNodeListServlet extends MonitoringWebsiteHtmlServlet {

	private static final long serialVersionUID = -7863752062438502326L;

	private static final String TITLE = "Monitoring - View";

	private final Logger logger;

	private final MonitoringService monitoringService;

	private final AuthenticationService authenticationService;

	private final MonitoringGuiLinkFactory monitoringGuiLinkFactory;

	private final CalendarUtil calendarUtil;

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
			final MonitoringGuiLinkFactory monitoringGuiLinkFactory,
			final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.monitoringService = monitoringService;
		this.authenticationService = authenticationService;
		this.monitoringGuiLinkFactory = monitoringGuiLinkFactory;
		this.calendarUtil = calendarUtil;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createMonitoringContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final MonitoringNodeTree<MonitoringNode> tree = new MonitoringNodeTree<MonitoringNode>(monitoringService.getCheckResults(sessionIdentifier));
			widgets.add(buildRows(request, sessionIdentifier, tree, tree.getRootNodes()));

			final ListWidget links = new ListWidget();
			if (monitoringService.hasMonitoringAdminPermission(sessionIdentifier)) {
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

	private Widget buildRows(final HttpServletRequest request, final SessionIdentifier sessionIdentifier, final MonitoringNodeTree<MonitoringNode> tree,
			final List<MonitoringNode> nodes) throws LoginRequiredException, MonitoringServiceException, MalformedURLException, UnsupportedEncodingException {
		if (nodes.isEmpty()) {
			return new VoidWidget();
		}
		Collections.sort(nodes, new MonitoringNodeComparator<MonitoringNode>());

		final UlWidget ul = new UlWidget();
		for (final MonitoringNode node : nodes) {
			final ListWidget row = new ListWidget();
			row.add(buildRow(request, sessionIdentifier, node));
			final List<MonitoringNode> childNodes = tree.getChildNodes(node.getId());
			row.add(buildRows(request, sessionIdentifier, tree, childNodes));
			ul.add(row);
		}
		return ul;
	}

	private Widget buildRow(final HttpServletRequest request, final SessionIdentifier sessionIdentifier, final MonitoringNode result) throws LoginRequiredException,
			MonitoringServiceException, MalformedURLException, UnsupportedEncodingException {
		final boolean isSilent = Boolean.TRUE.equals(result.getSilent());

		final ListWidget widgets = new ListWidget();
		final DivWidget row = new DivWidget(widgets);

		widgets.add("[");
		if (result.getResult() == null) {
			widgets.add(new SpanWidget("???").addClass("checkResultUnknown"));
		}
		else if (Boolean.TRUE.equals(result.getResult())) {
			widgets.add(new SpanWidget("OK").addClass("checkResultOk"));
		}
		else {
			widgets.add(new SpanWidget("FAIL").addClass("checkResultFail"));
		}
		widgets.add("] ");

		if (isSilent) {
			widgets.add("[");
			widgets.add(new SpanWidget("SILENT").addClass("checkSilent"));
			widgets.add("] ");
		}

		final ListWidget name = new ListWidget();
		name.add(result.getDescription());
		name.add(" (");
		name.add(result.getName());
		name.add(") ");
		final String description = "LastCheck: " + (result.getLastCheck() != null ? calendarUtil.toDateTimeString(result.getLastCheck()) : "-");
		widgets.add(new TooltipWidget(name).addTooltip(description));

		if (Boolean.FALSE.equals(result.getResult())) {
			widgets.add("(");
			widgets.add(result.getMessage() != null ? result.getMessage() : "-");
			widgets.add(")");
			widgets.add(" ");
		}

		if (monitoringService.hasMonitoringAdminPermission(sessionIdentifier)) {
			if (isSilent) {
				widgets.add(monitoringGuiLinkFactory.nodeUnsilent(request, result.getId()));
			}
			else {
				widgets.add(monitoringGuiLinkFactory.nodeSilent(request, result.getId()));
			}
			widgets.add(" ");
			widgets.add(monitoringGuiLinkFactory.nodeUpdate(request, result.getId()));
			widgets.add(" ");
			widgets.add(monitoringGuiLinkFactory.nodeDelete(request, result.getId()));
			widgets.add(" ");
			widgets.add(monitoringGuiLinkFactory.createNode(request, result.getId()));
			widgets.add(" ");
			widgets.add(monitoringGuiLinkFactory.checkNode(request, result.getId()));
		}

		return row;
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
			monitoringService.expectMonitoringViewOrAdminPermission(sessionIdentifier);
		}
		catch (final AuthenticationServiceException e) {
			throw new PermissionDeniedException(e);
		}
		catch (final MonitoringServiceException e) {
			throw new PermissionDeniedException(e);
		}
	}
}
