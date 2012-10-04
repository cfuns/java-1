package de.benjaminborbe.cron.gui.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.cron.api.CronController;
import de.benjaminborbe.cron.api.CronControllerException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.BrWidget;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.HtmlContentWidget;
import de.benjaminborbe.website.util.ListWidget;

@Singleton
public class CronGuiServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String PARAMETER_ACTION = "action";

	private static final String ACTION_START = "start";

	private static final String ACTION_STOP = "stop";

	private static final String TITLE = "Cron";

	private final CronController cronController;

	private final Logger logger;

	@Inject
	public CronGuiServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final CronController cronController,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, httpContextProvider, urlUtil);
		this.logger = logger;
		this.cronController = cronController;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			logger.trace("service");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));
			final String action = request.getParameter(PARAMETER_ACTION);
			if (ACTION_START.equals(action)) {
				cronController.start();
				widgets.add("started");
			}
			else if (ACTION_STOP.equals(action)) {
				cronController.stop();
				widgets.add("stopped");
			}
			else {
				widgets.add(createUsageWidget(request, response));
			}
			widgets.add(new BrWidget());
			widgets.add("cron is " + (cronController.isRunning() ? "" : "not ") + "running");
			return widgets;
		}
		catch (final CronControllerException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	protected Widget createUsageWidget(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		return new HtmlContentWidget("parameter " + PARAMETER_ACTION + " missing or value != " + ACTION_START + " or " + ACTION_STOP);
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

}
