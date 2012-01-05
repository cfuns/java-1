package de.benjaminborbe.bookmark.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.bookmark.api.Bookmark;
import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.dashboard.api.DashboardWidget;

@Singleton
public class BookmarkFavoriteDashboardWidget implements DashboardWidget {

	private final Logger logger;

	private final BookmarkService bookmarkService;

	@Inject
	public BookmarkFavoriteDashboardWidget(final Logger logger, final BookmarkService bookmarkService) {
		this.logger = logger;
		this.bookmarkService = bookmarkService;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		logger.debug("render");
		final PrintWriter out = response.getWriter();
		out.println("<ul>");
		out.println("<li>");
		for (final Bookmark bookmark : bookmarkService.getBookmarkFavoritie()) {
			out.println("<a href=\"" + bookmark.getUrl() + "\" target=\"_blank\">" + bookmark.getName() + "</a>");
		}
		out.println("</li>");
		out.println("</ul>");
	}

	@Override
	public String getTitle() {
		return "Bookmark-Favorites";
	}

}
