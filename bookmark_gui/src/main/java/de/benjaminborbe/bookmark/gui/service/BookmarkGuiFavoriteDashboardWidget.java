package de.benjaminborbe.bookmark.gui.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.bookmark.api.Bookmark;
import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.html.Target;

@Singleton
public class BookmarkGuiFavoriteDashboardWidget implements DashboardContentWidget {

	private final Logger logger;

	private final BookmarkService bookmarkService;

	private static final Target target = Target.BLANK;

	@Inject
	public BookmarkGuiFavoriteDashboardWidget(final Logger logger, final BookmarkService bookmarkService) {
		this.logger = logger;
		this.bookmarkService = bookmarkService;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.debug("render");
		final PrintWriter out = response.getWriter();
		out.println("<ul>");
		for (final Bookmark bookmark : bookmarkService.getBookmarkFavoritie()) {
			out.println("<li>");
			out.println("<a href=\"" + bookmark.getUrl() + "\" target=\"" + target + "\">" + bookmark.getName() + "</a>");
			out.println("</li>");
		}
		out.println("</ul>");
		out.println("<a href=\"" + request.getContextPath() + "/bookmark\">more</a>");
	}

	@Override
	public String getTitle() {
		return "Bookmark-Favorites";
	}

	@Override
	public long getPriority() {
		return 1;
	}

}
