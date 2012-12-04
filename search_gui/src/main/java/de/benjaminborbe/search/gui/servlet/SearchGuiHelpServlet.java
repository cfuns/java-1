package de.benjaminborbe.search.gui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.search.api.SearchSpecial;
import de.benjaminborbe.search.gui.SearchGuiConstants;
import de.benjaminborbe.search.gui.service.SearchGuiSpecialSearchRegistry;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.H3Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.BrWidget;

@Singleton
public class SearchGuiHelpServlet extends SearchGuiWebsiteHtmlServlet {

	private static final long serialVersionUID = -2168662340214933043L;

	private static final String TITLE = "Search - Help";

	private final UrlUtil urlUtil;

	private final SearchGuiSpecialSearchRegistry searchGuiSpecialSearchRegistry;

	@Inject
	public SearchGuiHelpServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final Provider<HttpContext> httpContextProvider,
			final UrlUtil urlUtil,
			final SearchGuiSpecialSearchRegistry searchGuiSpecialSearchRegistry) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.urlUtil = urlUtil;
		this.searchGuiSpecialSearchRegistry = searchGuiSpecialSearchRegistry;
	}

	@Override
	protected Widget createSearchContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {

		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(TITLE));
		widgets.add(new H2Widget("Google Chrome"));
		widgets.add("Settings -> Manage search engines... -> Add new search engine");
		widgets.add(new BrWidget());
		widgets.add("Url: " + urlUtil.buildBaseUrl(request) + "/" + SearchGuiConstants.NAME + "?q=%s&ie={inputEncoding}");
		widgets.add(new BrWidget());
		widgets.add(new BrWidget());

		widgets.add(new H2Widget("SpecialSearchs"));
		for (final SearchSpecial specialSearch : searchGuiSpecialSearchRegistry.getAll()) {
			final ListWidget list = new ListWidget();
			final List<String> names = new ArrayList<String>();
			for (final String name : specialSearch.getNames()) {
				names.add("type '" + name + ": [searchterm]'");
			}
			Collections.sort(names);
			list.add(new H3Widget(specialSearch.getClass().getSimpleName()));
			list.add(StringUtils.join(names, " or "));
			list.add(new BrWidget());
			list.add(new BrWidget());
			widgets.add(list);
		}
		return widgets;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

	@Override
	public boolean isLoginRequired() {
		return true;
	}
}
