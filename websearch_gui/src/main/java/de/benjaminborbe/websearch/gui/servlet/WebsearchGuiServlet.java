package de.benjaminborbe.websearch.gui.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

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
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.websearch.api.WebsearchConfiguration;
import de.benjaminborbe.websearch.api.WebsearchService;
import de.benjaminborbe.websearch.api.WebsearchServiceException;
import de.benjaminborbe.websearch.gui.util.WebsearchGuiLinkFactory;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class WebsearchGuiServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Websearch";

	private final Logger logger;

	private final WebsearchGuiLinkFactory websearchGuiLinkFactory;

	private final WebsearchService websearchService;

	private final AuthenticationService authenticationService;

	@Inject
	public WebsearchGuiServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final AuthorizationService authorizationService,
			final WebsearchGuiLinkFactory websearchGuiLinkFactory,
			final WebsearchService websearchService,
			final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.websearchGuiLinkFactory = websearchGuiLinkFactory;
		this.websearchService = websearchService;
		this.authenticationService = authenticationService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			{
				widgets.add(new H2Widget("Configurations"));
				final UlWidget ul = new UlWidget();
				final List<WebsearchConfiguration> groups = Lists.newArrayList(websearchService.getConfigurations(sessionIdentifier));
				for (final WebsearchConfiguration websearchConfiguration : groups) {
					final ListWidget list = new ListWidget();
					list.add(websearchConfiguration.getUrl().toExternalForm());
					list.add(" ");
					list.add(websearchGuiLinkFactory.configurationUpdate(request, websearchConfiguration.getId()));
					list.add(" ");
					list.add(websearchGuiLinkFactory.configurationDelete(request, websearchConfiguration.getId()));
					ul.add(list);
				}
				widgets.add(ul);
				widgets.add(websearchGuiLinkFactory.configurationCreate(request));
			}

			widgets.add(new H2Widget("Maintenance"));
			{
				final UlWidget ul = new UlWidget();
				ul.add(websearchGuiLinkFactory.refreshAll(request));
				ul.add(websearchGuiLinkFactory.clearIndex(request));
				ul.add(websearchGuiLinkFactory.expireAll(request));
				widgets.add(ul);
			}
			return widgets;
		}
		catch (final WebsearchServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		}
	}
}
