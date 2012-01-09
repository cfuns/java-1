package de.benjaminborbe.cron.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.SchedulerException;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.util.Quartz;
import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;

@Singleton
public class CronServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String PARAMETER_ACTION = "action";

	private static final String ACTION_START = "start";

	private static final String ACTION_STOP = "stop";

	private static final String TITLE = "Cron";

	private final Quartz quartz;

	@Inject
	public CronServlet(
			final Logger logger,
			final CssResourceRenderer cssResourceRenderer,
			final JavascriptResourceRenderer javascriptResourceRenderer,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final Quartz quartz,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider) {
		super(logger, cssResourceRenderer, javascriptResourceRenderer, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, httpContextProvider);
		this.quartz = quartz;
	}

	@Override
	protected void printContent(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.debug("service");
		final PrintWriter out = response.getWriter();
		out.println("<h1>" + getTitle() + "</h1>");
		final String action = request.getParameter(PARAMETER_ACTION);

		try {
			if (ACTION_START.equals(action)) {
				quartz.start();
				out.println("started");
			}
			else if (ACTION_STOP.equals(action)) {
				quartz.stop();
				out.println("stopped");
			}
			else {
				printUsage(request, response);
			}
		}
		catch (final SchedulerException e) {
		}
	}

	protected void printUsage(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("parameter " + PARAMETER_ACTION + " missing or value != " + ACTION_START + " or " + ACTION_STOP);
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

}
