package de.benjaminborbe.eventbus.gui.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.eventbus.api.Event.Type;
import de.benjaminborbe.eventbus.api.EventHandler;
import de.benjaminborbe.eventbus.api.EventbusService;
import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;

@Singleton
public class EventbusGuiServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Eventbus";

	private final EventbusService EventbusService;

	@Inject
	public EventbusGuiServlet(
			final Logger logger,
			final CssResourceRenderer cssResourceRenderer,
			final JavascriptResourceRenderer javascriptResourceRenderer,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final EventbusService EventbusService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider) {
		super(logger, cssResourceRenderer, javascriptResourceRenderer, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, httpContextProvider);
		this.EventbusService = EventbusService;
	}

	@Override
	protected void printContent(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.debug("service");
		final PrintWriter out = response.getWriter();
		out.println("<h1>" + getTitle() + "</h1>");
		out.println("EventHandlers:");
		out.println("<ul>");
		for (final Entry<Type<EventHandler>, List<EventHandler>> e : EventbusService.getHandlers().entrySet()) {
			out.println("<li>");
			final Type<EventHandler> type = e.getKey();
			final List<EventHandler> eventHandlers = e.getValue();
			out.println("Type: " + type.getClass().getName());
			for (final EventHandler eventHandler : eventHandlers) {
				out.println(" - " + eventHandler.getClass().getName());
			}
			out.println("</li>");
		}
		out.println("</ul>");
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}
}
