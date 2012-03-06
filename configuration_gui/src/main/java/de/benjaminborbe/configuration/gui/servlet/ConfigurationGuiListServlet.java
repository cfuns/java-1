package de.benjaminborbe.configuration.gui.servlet;

import java.io.IOException;
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
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.HtmlListWidget;

@Singleton
public class ConfigurationGuiListServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Configuration - List";

	private final ConfigurationService configurationService;

	@Inject
	public ConfigurationGuiListServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final Provider<HttpContext> httpContextProvider,
			final ConfigurationService configurationService,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, httpContextProvider, redirectUtil, urlUtil);
		this.configurationService = configurationService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			logger.trace("printContent");
			final HtmlListWidget widgets = new HtmlListWidget();
			widgets.add(new H1Widget(getTitle()));
			widgets.add("<table>");
			widgets.add("<tr>");
			widgets.add("<th>Name</th>");
			widgets.add("<th>Description</th>");
			widgets.add("<th>Type</th>");
			widgets.add("<th>DefaultValue</th>");
			widgets.add("<th>CurrentValue</th>");
			widgets.add("</tr>");
			for (final Configuration<?> configuration : configurationService.listConfigurations()) {
				widgets.add("<tr>");
				widgets.add("<td>");
				widgets.add(configuration.getName());
				widgets.add("</td>");
				widgets.add("<td>");
				widgets.add(configuration.getDescription());
				widgets.add("</td>");
				widgets.add("<td>");
				widgets.add(String.valueOf(configuration.getDefaultValue()));
				widgets.add("</td>");
				widgets.add("<td>");
				widgets.add(configuration.getType().getSimpleName());
				widgets.add("</td>");
				widgets.add("<td>");
				widgets.add(String.valueOf(configurationService.getConfigurationValue(configuration)));
				widgets.add("</td>");
				widgets.add("</tr>");
			}
			widgets.add("</table>");
			return widgets;
		}
		catch (final ConfigurationServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}
}
