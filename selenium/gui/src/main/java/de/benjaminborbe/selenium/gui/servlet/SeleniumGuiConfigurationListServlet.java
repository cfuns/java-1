package de.benjaminborbe.selenium.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.selenium.api.SeleniumService;
import de.benjaminborbe.selenium.api.SeleniumServiceException;
import de.benjaminborbe.selenium.configuration.xml.api.SeleniumConfigurationXmlService;
import de.benjaminborbe.selenium.configuration.xml.api.SeleniumConfigurationXmlServiceException;
import de.benjaminborbe.selenium.gui.util.SeleniumConfigurationComparator;
import de.benjaminborbe.selenium.gui.util.SeleniumGuiLinkFactory;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ComparatorUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import de.benjaminborbe.website.widget.BrWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Singleton
public class SeleniumGuiConfigurationListServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Selenium - Configuration - List";

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final SeleniumService seleniumService;

	private final SeleniumGuiLinkFactory seleniumGuiLinkFactory;

	private final ComparatorUtil comparatorUtil;

	private final SeleniumConfigurationXmlService seleniumConfigurationXmlService;

	@Inject
	public SeleniumGuiConfigurationListServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final CacheService cacheService,
		final SeleniumService seleniumService,
		final SeleniumGuiLinkFactory seleniumGuiLinkFactory,
		final ComparatorUtil comparatorUtil,
		final SeleniumConfigurationXmlService seleniumConfigurationXmlService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.seleniumService = seleniumService;
		this.seleniumGuiLinkFactory = seleniumGuiLinkFactory;
		this.comparatorUtil = comparatorUtil;
		this.seleniumConfigurationXmlService = seleniumConfigurationXmlService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(
		final HttpServletRequest request, final HttpServletResponse response, final HttpContext context
	) throws IOException, PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

			final UlWidget ul = new UlWidget();
			final List<SeleniumConfiguration> seleniumConfigurations = comparatorUtil.sort(seleniumService.getSeleniumConfigurations(sessionIdentifier), new SeleniumConfigurationComparator());
			if (seleniumConfigurations.isEmpty()) {
				widgets.add("no configuration found");
				widgets.add(new BrWidget());
			} else {
				for (final SeleniumConfiguration seleniumConfiguration : seleniumConfigurations) {
					final SeleniumConfigurationIdentifier seleniumConfigurationIdentifier = seleniumConfiguration.getId();

					final ListWidget row = new ListWidget();
					row.add(seleniumConfiguration.getName());
					row.add(" ");
					row.add(seleniumGuiLinkFactory.configurationRun(request, seleniumConfigurationIdentifier));
					if (seleniumConfigurationXmlService.isXmlConfiguration(sessionIdentifier, seleniumConfigurationIdentifier)) {
						row.add(" ");
						row.add(seleniumGuiLinkFactory.configurationShow(request, seleniumConfigurationIdentifier));
						row.add(" ");
						row.add(seleniumGuiLinkFactory.configurationUpdate(request, seleniumConfigurationIdentifier));
						row.add(" ");
						row.add(seleniumGuiLinkFactory.configurationXmlDelete(request, seleniumConfigurationIdentifier));
					}
					ul.add(row);
				}
				widgets.add(ul);
			}

			widgets.add(seleniumGuiLinkFactory.configurationXmlRun(request));
			widgets.add(new BrWidget());
			widgets.add(seleniumGuiLinkFactory.configurationXmlUpload(request));
			widgets.add(new BrWidget());

			return widgets;
		} catch (SeleniumServiceException | SeleniumConfigurationXmlServiceException | AuthenticationServiceException e) {
			return new ExceptionWidget(e);
		}
	}
}
