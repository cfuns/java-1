package de.benjaminborbe.configuration.gui.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.configuration.api.Configuration;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;
import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;

@Singleton
public class ConfigurationGuiListServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Configuration - List";

	private final ConfigurationService configurationService;

	@Inject
	public ConfigurationGuiListServlet(
			final Logger logger,
			final CssResourceRenderer cssResourceRenderer,
			final JavascriptResourceRenderer javascriptResourceRenderer,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final Provider<HttpContext> httpContextProvider,
			final ConfigurationService configurationService) {
		super(logger, cssResourceRenderer, javascriptResourceRenderer, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, httpContextProvider);
		this.configurationService = configurationService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected void printContent(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			final PrintWriter out = response.getWriter();
			logger.trace("printContent");
			out.println("<h1>" + getTitle() + "</h1>");

			out.println("<table>");
			out.println("<tr>");
			out.println("<th>Name</th>");
			out.println("<th>Description</th>");
			out.println("<th>Type</th>");
			out.println("<th>DefaultValue</th>");
			out.println("<th>CurrentValue</th>");
			out.println("</tr>");
			for (final Configuration<?> configuration : configurationService.listConfigurations()) {
				out.println("<tr>");
				out.println("<td>");
				out.println(configuration.getName());
				out.println("</td>");
				out.println("<td>");
				out.println(configuration.getDescription());
				out.println("</td>");
				out.println("<td>");
				out.println(configuration.getDefaultValue());
				out.println("</td>");
				out.println("<td>");
				out.println(configuration.getType().getSimpleName());
				out.println("</td>");
				out.println("<td>");
				out.println(configurationService.getConfigurationValue(configuration));
				out.println("</td>");
				out.println("</tr>");
			}
			out.println("</table>");
		}
		catch (final ConfigurationServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		}
	}
}
