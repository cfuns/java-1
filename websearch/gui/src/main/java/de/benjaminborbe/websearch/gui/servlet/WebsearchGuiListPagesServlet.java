package de.benjaminborbe.websearch.gui.servlet;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
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
import de.benjaminborbe.tools.util.ComparatorBase;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.websearch.api.WebsearchPage;
import de.benjaminborbe.websearch.api.WebsearchService;
import de.benjaminborbe.websearch.api.WebsearchServiceException;
import de.benjaminborbe.websearch.gui.util.WebsearchGuiLinkFactory;
import de.benjaminborbe.website.link.LinkWidget;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Singleton
public class WebsearchGuiListPagesServlet extends WebsiteHtmlServlet {

	private static final int PAGE_LIMIT = 100;

	private final class PageComparator extends ComparatorBase<WebsearchPage, String> {

		@Override
		public String getValue(final WebsearchPage o) {
			return o.getUrl();
		}
	}

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Websearch - List Pages";

	private final WebsearchService websearchService;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final WebsearchGuiLinkFactory websearchGuiLinkFactory;

	private final CalendarUtil calendarUtil;

	@Inject
	public WebsearchGuiListPagesServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final WebsearchService websearchService,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final WebsearchGuiLinkFactory websearchGuiLinkFactory,
		final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.calendarUtil = calendarUtil;
		this.websearchService = websearchService;
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.websearchGuiLinkFactory = websearchGuiLinkFactory;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));
			final UlWidget ul = new UlWidget();
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final List<WebsearchPage> pages = sortPages(websearchService.getPages(sessionIdentifier));
			if (pages.size() == PAGE_LIMIT) {
				widgets.add("more than " + PAGE_LIMIT + " pages found, display only first " + PAGE_LIMIT);
			}
			for (final WebsearchPage page : pages) {
				ul.add(buildPageWidget(page, request));
			}
			widgets.add(ul);
			return widgets;
		} catch (final WebsearchServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		} catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	protected List<WebsearchPage> sortPages(final Collection<WebsearchPage> pages) {
		final List<WebsearchPage> result = new ArrayList<WebsearchPage>(pages);
		Collections.sort(result, new PageComparator());
		return result;
	}

	protected Widget buildPageWidget(final WebsearchPage page, final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		final ListWidget widgets = new ListWidget();
		final String url = page.getUrl();
		widgets.add(new LinkWidget(url, url));
		widgets.add(" ");
		if (page.getLastVisit() != null) {
			widgets.add(calendarUtil.toDateTimeString(page.getLastVisit()));
		} else {
			widgets.add("-");
		}
		widgets.add(" ");
		widgets.add(websearchGuiLinkFactory.pageExpire(request, page.getId()));
		widgets.add(" ");
		widgets.add(websearchGuiLinkFactory.pageRefresh(request, page.getId()));
		return widgets;
	}
}
