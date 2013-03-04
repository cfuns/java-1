package de.benjaminborbe.dashboard.gui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.dashboard.api.DashboardIdentifier;
import de.benjaminborbe.dashboard.api.DashboardService;
import de.benjaminborbe.dashboard.api.DashboardServiceException;
import de.benjaminborbe.dashboard.gui.service.DashboardGuiWidgetRegistry;
import de.benjaminborbe.dashboard.gui.util.DashboardGuiContentWidgetComparatorTitle;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormCheckboxWidget;
import de.benjaminborbe.website.form.FormInputHiddenWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;

@Singleton
public class DashboardGuiConfigureServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 2904738168601881766L;

	private static final String TITLE = "Dashboard - Configure";

	private final DashboardGuiWidgetRegistry dashboardGuiWidgetRegistry;

	private final DashboardService dashboardService;

	private final AuthenticationService authenticationService;

	private final Logger logger;

	private final AuthorizationService authorizationService;

	@Inject
	public DashboardGuiConfigureServlet(
			final Logger logger,
			final DashboardService dashboardService,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final Provider<HttpContext> httpContextProvider,
			final UrlUtil urlUtil,
			final DashboardGuiWidgetRegistry dashboardGuiWidgetRegistry,
			final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.dashboardService = dashboardService;
		this.dashboardGuiWidgetRegistry = dashboardGuiWidgetRegistry;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
		this.logger = logger;
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
			final List<DashboardContentWidget> list = new ArrayList<DashboardContentWidget>(dashboardGuiWidgetRegistry.getAll());
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final String action = request.getParameter("action");
			if ("update".equals(action)) {
				for (final DashboardContentWidget w : list) {
					if ("true".equals(request.getParameter(w.getTitle()))) {
						dashboardService.selectDashboard(sessionIdentifier, new DashboardIdentifier(w.getTitle()));
					}
					else {
						dashboardService.deselectDashboard(sessionIdentifier, new DashboardIdentifier(w.getTitle()));
					}
				}
			}

			final Set<DashboardIdentifier> di = new HashSet<DashboardIdentifier>(dashboardService.getSelectedDashboards(sessionIdentifier));
			Collections.sort(list, new DashboardGuiContentWidgetComparatorTitle());
			final FormWidget form = new FormWidget();
			for (final DashboardContentWidget w : list) {
				final FormCheckboxWidget input = new FormCheckboxWidget(w.getTitle()).addLabel(w.getTitle());
				final DashboardIdentifier dashboardIdentifier = new DashboardIdentifier(w.getTitle());
				input.setCheck(di.contains(dashboardIdentifier));
				form.addFormInputWidget(input);
			}
			form.addFormInputWidget(new FormInputSubmitWidget("update"));
			form.addFormInputWidget(new FormInputHiddenWidget("action").addValue("update"));
			widgets.add(form);
			return widgets;
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		catch (final DashboardServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
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
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(DashboardService.PERMISSION);
			authorizationService.expectPermission(sessionIdentifier, permissionIdentifier);
		}
		catch (final AuthorizationServiceException | AuthenticationServiceException e) {
			throw new PermissionDeniedException(e);
		}
	}

}
