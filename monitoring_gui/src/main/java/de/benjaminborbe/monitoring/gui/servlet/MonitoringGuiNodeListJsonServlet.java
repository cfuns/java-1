package de.benjaminborbe.monitoring.gui.servlet;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.monitoring.api.MonitoringNode;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.api.MonitoringServiceException;
import de.benjaminborbe.monitoring.gui.MonitoringGuiConstants;
import de.benjaminborbe.monitoring.tools.MonitoringNodeTree;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.WebsiteJsonServlet;

@Singleton
public class MonitoringGuiNodeListJsonServlet extends WebsiteJsonServlet {

	private static final long serialVersionUID = 1844470197045483190L;

	private final Logger logger;

	private final MonitoringService monitoringService;

	private final CalendarUtil calendarUtil;

	@Inject
	public MonitoringGuiNodeListJsonServlet(
			final Logger logger,
			final UrlUtil urlUtil,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider,
			final MonitoringService monitoringService,
			final ParseUtil parseUtil) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.monitoringService = monitoringService;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException, LoginRequiredException {
		try {
			logger.debug("doService");
			final String token = request.getParameter(MonitoringGuiConstants.PARAMETER_AUTH_TOKEN);
			final MonitoringNodeTree<MonitoringNode> tree = new MonitoringNodeTree<MonitoringNode>(monitoringService.getCheckResults(token));
			final JSONObject object = new JSONObject();

			final JSONArray nodeResults = new JSONArray();
			final List<MonitoringNode> list = tree.getRootNodes();
			handle(nodeResults, list, tree);

			printJson(response, object);
		}
		catch (final MonitoringServiceException e) {
			printException(response, e);
		}
	}

	@SuppressWarnings("unchecked")
	public void handle(final JSONArray nodeResults, final List<MonitoringNode> list, final MonitoringNodeTree<MonitoringNode> tree) {
		for (final MonitoringNode node : list) {
			if (Boolean.TRUE.equals(node.getActive())) {
				final JSONObject nodeResult = new JSONObject();

				nodeResult.put("id", node.getId());
				nodeResult.put("message", node.getMessage());
				nodeResult.put("name", node.getName());
				nodeResult.put("description", node.getDescription());
				nodeResult.put("lastCheck", calendarUtil.toDateTimeString(node.getLastCheck()));
				nodeResult.put("failureCounter", node.getFailureCounter());
				nodeResult.put("result", node.getResult());
				nodeResult.put("active", node.getActive());
				nodeResult.put("silent", node.getSilent());
				nodeResult.put("checkType", node.getCheckType());
				nodeResults.add(nodeResult);
				if (Boolean.TRUE.equals(node.getResult())) {
					handle(nodeResults, tree.getChildNodes(node.getId()), tree);
				}
			}
		}
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException {
		final String token = request.getParameter(MonitoringGuiConstants.PARAMETER_AUTH_TOKEN);
		logger.debug("doCheckPermission");
		try {
			monitoringService.expectAuthToken(token);
		}
		catch (final MonitoringServiceException e) {
			throw new PermissionDeniedException(e);
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

}
