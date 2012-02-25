package de.benjaminborbe.bookmark.gui.servlet;

import java.io.IOException;
import java.net.URL;
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
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.bookmark.api.Bookmark;
import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.bookmark.api.BookmarkServiceException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.html.Target;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.link.LinkWidget;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class BookmarkGuiListServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String PAGE_TITLE = "Bookmarks";

	private static final Target target = Target.BLANK;

	private final BookmarkService bookmarkService;

	@Inject
	public BookmarkGuiListServlet(
			final Logger logger,
			final BookmarkService bookmarkService,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, httpContextProvider);
		this.bookmarkService = bookmarkService;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException {
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));
		widgets.add(createLinksWidget(request, response, context));
		return widgets;
	}

	protected Widget createLinksWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException, PermissionDeniedException {
		try {
			final ListWidget widgets = new ListWidget();
			widgets.add(new H2Widget("Links"));
			final UlWidget ul = new UlWidget();
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			for (final Bookmark bookmark : bookmarkService.getBookmarks(sessionIdentifier)) {
				final ListWidget b = new ListWidget();
				b.add(new LinkWidget(new URL(bookmark.getUrl()), bookmark.getName()).addTarget(target));
				b.add(" ");
				b.add("[" + keywordsToString(bookmark) + "]");
				ul.add(b);
			}
			widgets.add(ul);
			return widgets;
		}
		catch (final AuthenticationServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		catch (final BookmarkServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	protected String keywordsToString(final Bookmark bookmark) {
		final List<String> keywords = new ArrayList<String>(bookmark.getKeywords());
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
