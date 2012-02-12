package de.benjaminborbe.systemstatus.gui.servlet;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Enumeration;

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
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.br.BrWidget;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class SystemstatusGuiServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Systemstatus";

	@Inject
	public SystemstatusGuiServlet(
			final Logger logger,
			final CssResourceRenderer cssResourceRenderer,
			final JavascriptResourceRenderer javascriptResourceRenderer,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider) {
		super(logger, cssResourceRenderer, javascriptResourceRenderer, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, httpContextProvider);
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected void printContent(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.trace("printContent");
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));

		{
			widgets.add(new H2Widget("Session-Data"));
			@SuppressWarnings("unchecked")
			final Enumeration<String> e = request.getSession().getAttributeNames();
			if (e.hasMoreElements()) {
				final UlWidget ul = new UlWidget();
				while (e.hasMoreElements()) {
					final String name = e.nextElement();
					ul.add(name + " = " + request.getSession().getAttribute(name));
				}
				widgets.add(ul);
			}
			else {
				widgets.add("no data in session");
			}
		}
		{
			widgets.add(new H2Widget("Memory"));
			widgets.add(getMemoryState());
			widgets.add(new BrWidget());
			Runtime.getRuntime().gc();
			widgets.add(getMemoryState());
			widgets.add(new BrWidget());
		}

		widgets.render(request, response, context);
	}

	protected String getMemoryState() {
		final StringWriter msg = new StringWriter();
		msg.append("Memory state bevor cleanup: ");
		msg.append("used=" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));
		msg.append(", ");
		msg.append("free=" + (Runtime.getRuntime().freeMemory() / (1024 * 1024)));
		msg.append(", ");
		msg.append("total=" + (Runtime.getRuntime().totalMemory() / (1024 * 1024)));
		msg.append(", ");
		msg.append("max=" + (Runtime.getRuntime().maxMemory() / (1024 * 1024)));
		return msg.toString();
	}
}
