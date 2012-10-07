package de.benjaminborbe.monitoring.gui.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.api.MonitoringServiceException;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.link.LinkRelativWidget;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.BrWidget;

@Singleton
public class MonitoringGuiSilentCheckServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = -3368317580182944276L;

	private static final String TITLE = "Monitoring - Deactivate Check";

	private static final String PARAMETER_CHECK = "check";

	private final MonitoringService monitoringService;

	private final AuthenticationService authenticationService;

	@Inject
	public MonitoringGuiSilentCheckServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final MonitoringService monitoringService,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, httpContextProvider, urlUtil);
		this.monitoringService = monitoringService;
		this.authenticationService = authenticationService;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException {
		try {
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final String checkName = request.getParameter(PARAMETER_CHECK);
			if (checkName != null) {
				final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
				monitoringService.silentCheck(sessionIdentifier, checkName);
				widgets.add("check " + checkName + " silent");
			}
			else {
				final FormWidget form = new FormWidget();
				form.addFormInputWidget(new FormInputTextWidget(PARAMETER_CHECK).addLabel("CheckName").addPlaceholder("CheckName ..."));
				form.addFormInputWidget(new FormInputSubmitWidget("silent"));
				widgets.add(form);
			}
			widgets.add(new BrWidget());
			widgets.add(new LinkRelativWidget(request, "/monitoring", "back"));

			return widgets;
		}
		catch (final MonitoringServiceException e) {
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			return exceptionWidget;
		}
		catch (final AuthenticationServiceException e) {
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			return exceptionWidget;
		}
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

}
