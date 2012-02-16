package de.benjaminborbe.monitoring.gui.servlet;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;

@Singleton
public class MonitoringGuiSilentCheckServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = -3368317580182944276L;

	private static final String TITLE = "Monitoring - Deactivate Check";

	private static final String PARAMETER_CHECK = "check";

	private final MonitoringService monitoringService;

	@Inject
	public MonitoringGuiSilentCheckServlet(
			final Logger logger,
			final CssResourceRenderer cssResourceRenderer,
			final JavascriptResourceRenderer javascriptResourceRenderer,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final MonitoringService monitoringService) {
		super(logger, cssResourceRenderer, javascriptResourceRenderer, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, httpContextProvider);
		this.monitoringService = monitoringService;
	}

	@Override
	protected void printContent(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));

		final String checkName = request.getParameter(PARAMETER_CHECK);
		if (checkName != null) {
			monitoringService.silentCheck(checkName);
			widgets.add("check " + checkName + " silent");
		}
		else {
			final FormWidget form = new FormWidget("");
			form.addFormInputWidget(new FormInputTextWidget(PARAMETER_CHECK).addLabel("CheckName").addPlaceholder("CheckName ..."));
			form.addFormInputWidget(new FormInputSubmitWidget("silent"));
			widgets.add(form);
		}

		widgets.render(request, response, context);
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

}
