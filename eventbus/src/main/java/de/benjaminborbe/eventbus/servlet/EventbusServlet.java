package de.benjaminborbe.eventbus.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.eventbus.api.Event.Type;
import de.benjaminborbe.eventbus.api.EventHandler;
import de.benjaminborbe.eventbus.service.EventBusServiceImpl;
import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;

@Singleton
public class EventbusServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "EventBus";

	private final EventBusServiceImpl eventBusServiceImpl;

	@Inject
	public EventbusServlet(final Logger logger, final CssResourceRenderer cssResourceRenderer, final JavascriptResourceRenderer javascriptResourceRenderer, final EventBusServiceImpl eventBusServiceImpl) {
		super(logger, cssResourceRenderer, javascriptResourceRenderer);
		this.eventBusServiceImpl = eventBusServiceImpl;
	}

	@Override
	protected void printBody(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		logger.debug("service");
		response.setContentType("text/plain");
		final PrintWriter out = response.getWriter();
		out.println("EventHandlers:");
		for (final Entry<Type<EventHandler>, List<EventHandler>> e : eventBusServiceImpl.getHandlers().entrySet()) {
			final Type<EventHandler> type = e.getKey();
			final List<EventHandler> eventHandlers = e.getValue();
			out.println("Type: " + type.getClass().getName());
			for (final EventHandler eventHandler : eventHandlers) {
				out.println(" - " + eventHandler.getClass().getName());
			}
			out.println("");
		}
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}
}
