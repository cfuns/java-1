package de.benjaminborbe.search.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.search.api.SearchWidget;
import de.benjaminborbe.search.gui.util.SearchGuiLinkFactory;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class SearchGuiServlet extends SearchGuiWebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Search";

	private final SearchWidget searchWidget;

	private final SearchGuiLinkFactory searchGuiLinkFactory;

	@Inject
	public SearchGuiServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final SearchWidget searchWidget,
		final NavigationWidget navigationWidget,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final SearchGuiLinkFactory searchGuiLinkFactory,
		final CacheService cacheService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.searchWidget = searchWidget;
		this.searchGuiLinkFactory = searchGuiLinkFactory;
	}

	@Override
	protected Collection<Widget> getWidgets() {
		final Set<Widget> result = new HashSet<>();
		result.add(searchWidget);
		return result;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	public boolean isLoginRequired() {
		return true;
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

	@Override
	protected Widget createSearchContentWidget(final HttpServletRequest request) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));
		widgets.add(searchWidget);

		final ListWidget links = new ListWidget();
		links.add(searchGuiLinkFactory.searchHelp(request));
		widgets.add(links);
		return widgets;
	}
}
