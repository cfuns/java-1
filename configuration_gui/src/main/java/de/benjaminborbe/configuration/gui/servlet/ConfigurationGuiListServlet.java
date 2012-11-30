package de.benjaminborbe.configuration.gui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;
import de.benjaminborbe.configuration.gui.ConfigurationGuiConstants;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ComparatorBase;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.HtmlListWidget;

@Singleton
public class ConfigurationGuiListServlet extends WebsiteHtmlServlet {

	private final class ComparatorImplementation extends ComparatorBase<ConfigurationDescription, String> {

		@Override
		public String getValue(final ConfigurationDescription o) {
			return o.getName() != null ? o.getName().toLowerCase() : null;
		}
	}

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Configuration - List";

	private final ConfigurationService configurationService;

	private final Logger logger;

	private final UrlUtil urlUtil;

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
			final UrlUtil urlUtil,
			final AuthorizationService authorizationService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.configurationService = configurationService;
		this.logger = logger;
		this.urlUtil = urlUtil;
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
			widgets.add("<th></th>");
			widgets.add("</tr>");
			for (final ConfigurationDescription configuration : getConfigurationDescriptions()) {
				widgets.add("<tr>");
				widgets.add("<td>");
				widgets.add(configuration.getName());
				widgets.add("</td>");
				widgets.add("<td>");
				widgets.add(configuration.getDescription());
				widgets.add("</td>");
				widgets.add("<td>");
				widgets.add(configuration.getType());
				widgets.add("</td>");
				widgets.add("<td>");
				widgets.add(configuration.getDefaultValueAsString());
				widgets.add("</td>");
				widgets.add("<td>");
				widgets.add(String.valueOf(configurationService.getConfigurationValue(configuration)));
				widgets.add("</td>");
				widgets.add("<td>");
				widgets.add(new LinkRelativWidget(urlUtil, request, "/" + ConfigurationGuiConstants.NAME + ConfigurationGuiConstants.URL_UPDATE, new MapParameter().add(
						ConfigurationGuiConstants.PARAMETER_CONFIGURATION_ID, String.valueOf(configuration.getId())), "edit"));
				widgets.add("</td>");
				widgets.add("</tr>");
			}
			widgets.add("</table>");
			return widgets;
		}
		catch (final ConfigurationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	private List<ConfigurationDescription> getConfigurationDescriptions() {
		final List<ConfigurationDescription> list = new ArrayList<ConfigurationDescription>(configurationService.listConfigurations());
		Collections.sort(list, new ComparatorImplementation());
		return list;
	}
}
