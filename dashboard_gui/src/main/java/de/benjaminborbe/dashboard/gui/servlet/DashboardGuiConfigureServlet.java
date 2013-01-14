package de.benjaminborbe.dashboard.gui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.dashboard.gui.service.DashboardGuiWidgetRegistry;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormCheckboxWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;

@Singleton
public class DashboardGuiConfigureServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 2904738168601881766L;

	private static final String TITLE = "Dashboard - Configure";

	private final DashboardGuiWidgetRegistry dashboardGuiWidgetRegistry;

	@Inject
	public DashboardGuiConfigureServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final Provider<HttpContext> httpContextProvider,
			final UrlUtil urlUtil,
			final DashboardGuiWidgetRegistry dashboardGuiWidgetRegistry) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.dashboardGuiWidgetRegistry = dashboardGuiWidgetRegistry;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {

		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));

		final List<DashboardContentWidget> list = new ArrayList<DashboardContentWidget>(dashboardGuiWidgetRegistry.getAll());

		final FormWidget form = new FormWidget();
		for (final DashboardContentWidget w : list) {
			final FormCheckboxWidget input = new FormCheckboxWidget(w.getTitle()).addLabel(w.getTitle());
			form.addFormInputWidget(input);
		}
		form.addFormInputWidget(new FormInputSubmitWidget("update"));
		widgets.add(form);
		return widgets;
	}
}
