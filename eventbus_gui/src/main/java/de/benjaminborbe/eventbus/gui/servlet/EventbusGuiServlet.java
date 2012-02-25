package de.benjaminborbe.eventbus.gui.servlet;

import java.io.IOException;
import java.io.StringWriter;
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
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.LiWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class EventbusGuiServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Eventbus";

	private final EventbusService EventbusService;

	@Inject
	public EventbusGuiServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final EventbusService EventbusService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, httpContextProvider);
		this.EventbusService = EventbusService;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final ListWidget widgets = new ListWidget();
		logger.trace("service");
		widgets.add(new H1Widget(getTitle()));
		widgets.add("EventHandlers:");
		final UlWidget ul = new UlWidget();
		for (final Entry<Type<EventHandler>, List<EventHandler>> e : EventbusService.getHandlers().entrySet()) {
			final StringWriter content = new StringWriter();
			final Type<EventHandler> type = e.getKey();
			final List<EventHandler> eventHandlers = e.getValue();
			content.append("Type: " + type.getClass().getName());
			for (final EventHandler eventHandler : eventHandlers) {
				content.append(" - " + eventHandler.getClass().getName());
			}
			ul.add(new LiWidget(content.toString()));
		}
		widgets.add(ul);
		return widgets;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}
}
