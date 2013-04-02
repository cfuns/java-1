package de.benjaminborbe.wiki.gui.servlet;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import de.benjaminborbe.website.widget.BrWidget;
import de.benjaminborbe.wiki.api.WikiService;
import de.benjaminborbe.wiki.api.WikiServiceException;
import de.benjaminborbe.wiki.api.WikiSpaceIdentifier;
import de.benjaminborbe.wiki.gui.WikiGuiConstants;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Singleton
public class WikiGuiSpaceListServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Wiki - Spaces";

	private final WikiService wikiService;

	private final UrlUtil urlUtil;

	private final Logger logger;

	@Inject
	public WikiGuiSpaceListServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final WikiService wikiService,
		final AuthorizationService authorizationService,
		final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.wikiService = wikiService;
		this.logger = logger;
		this.urlUtil = urlUtil;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, RedirectException {
		try {
			logger.debug("render " + getClass().getSimpleName());
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			// space list
			{
				final Collection<WikiSpaceIdentifier> spaces = wikiService.getSpaceIdentifiers();
				if (spaces.size() > 0) {
					final UlWidget ul = new UlWidget();
					for (final WikiSpaceIdentifier space : spaces) {
						ul.add(new LinkRelativWidget(urlUtil, request, "/wiki/page/list", new MapParameter().add(WikiGuiConstants.PARAMETER_SPACE_ID, space.getId()), space.getId()));
					}
					widgets.add(ul);
				} else {
					widgets.add("no space exists");
					widgets.add(new BrWidget());
				}
				widgets.add(new LinkRelativWidget(request, "/wiki/space/create", "add space"));
			}
			return widgets;
		} catch (final WikiServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}
}
