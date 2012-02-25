package de.benjaminborbe.monitoring.gui.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.monitoring.api.MonitoringWidget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;

@Singleton
public class MonitoringGuiServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = -3368317580182944276L;

	private static final String TITLE = "Monitoring";

	private final MonitoringWidget monitoringWidget;

	@Inject
	public MonitoringGuiServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final MonitoringWidget monitoringWidget,
			final Provider<HttpContext> httpContextProvider) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, httpContextProvider);
		this.monitoringWidget = monitoringWidget;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException {
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));
		widgets.add(monitoringWidget);
		return widgets;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Collection<Widget> getWidgets() {
		final Set<Widget> result = new HashSet<Widget>(super.getWidgets());
		result.add(monitoringWidget);
		return result;
	}

}
