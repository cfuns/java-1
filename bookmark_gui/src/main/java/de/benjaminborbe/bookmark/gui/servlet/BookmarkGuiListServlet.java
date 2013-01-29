package de.benjaminborbe.bookmark.gui.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.bookmark.api.Bookmark;
import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.bookmark.api.BookmarkServiceException;
import de.benjaminborbe.bookmark.gui.widget.BookmarkCreateLink;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.html.Target;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;
import de.benjaminborbe.website.link.LinkWidget;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class BookmarkGuiListServlet extends BookmarkGuiBaseServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String PAGE_TITLE = "Bookmarks";

	private static final Target target = Target.BLANK;

	private final BookmarkService bookmarkService;

	private final UrlUtil urlUtil;

	private final AuthenticationService authenticationService;

	private final Logger logger;

	@Inject
	public BookmarkGuiListServlet(
			final Logger logger,
			final BookmarkService bookmarkService,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final AuthorizationService authorizationService,
			final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.bookmarkService = bookmarkService;
		this.urlUtil = urlUtil;
		this.authenticationService = authenticationService;
		this.logger = logger;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, LoginRequiredException {
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));
		widgets.add(createLinksWidget(request, response, context));
		return widgets;
	}

	protected Widget createLinksWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws PermissionDeniedException,
			LoginRequiredException {
		try {
			final ListWidget widgets = new ListWidget();
			widgets.add(new H2Widget("Links"));
			final UlWidget ul = new UlWidget();
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			for (final Bookmark bookmark : bookmarkService.getBookmarks(sessionIdentifier)) {
				final ListWidget b = new ListWidget();
				b.add(new LinkWidget(urlUtil.buildUrl(request, bookmark.getUrl()), bookmark.getName()).addTarget(target));
				b.add(" | ");
				b.add(new LinkWidget(urlUtil.buildUrl(request, bookmark.getUrl()), bookmark.getUrl()).addTarget(target));
				b.add(" | [");
				b.add(new LinkWidget(urlUtil.buildUrl(request, bookmark.getUrl()), keywordsToString(bookmark)).addTarget(target));
				b.add("] | ");
				b.add(new LinkRelativWidget(urlUtil, request, "/bookmark/update", new MapParameter().add("url", bookmark.getUrl()), "edit"));
				b.add(" ");
				b.add(new LinkRelativWidget(urlUtil, request, "/bookmark/delete", new MapParameter().add("url", bookmark.getUrl()), "delete"));
				ul.add(b);
			}
			widgets.add(ul);
			widgets.add(new BookmarkCreateLink(request));
			return widgets;
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		catch (final BookmarkServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		catch (final MalformedURLException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		catch (final UnsupportedEncodingException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	protected String keywordsToString(final Bookmark bookmark) {
		final List<String> keywords = new ArrayList<String>();
		if (bookmark.getKeywords() != null) {
			keywords.addAll(bookmark.getKeywords());
		}
		Collections.sort(keywords);
		return StringUtils.join(bookmark.getKeywords(), ",");
	}

	@Override
	protected String getTitle() {
		return PAGE_TITLE;
	}

	@Override
	protected Collection<Widget> getWidgets() {
		return new HashSet<Widget>();
	}

}
