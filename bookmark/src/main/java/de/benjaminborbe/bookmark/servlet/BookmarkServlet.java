package de.benjaminborbe.bookmark.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.google.inject.Singleton;

import de.benjaminborbe.bookmark.api.Bookmark;
import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.html.Target;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;

@Singleton
public class BookmarkServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String PAGE_TITLE = "Bookmarks";

	private static final Target target = Target.BLANK;

	private final BookmarkService bookmarkService;

	@Inject
	public BookmarkServlet(final Logger logger, final CssResourceRenderer cssResourceRenderer, final JavascriptResourceRenderer javascriptResourceRenderer, final BookmarkService bookmarkService) {
		super(logger, cssResourceRenderer, javascriptResourceRenderer);
		this.bookmarkService = bookmarkService;
	}

	@Override
	protected void printBody(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<body>");
		out.println("<h1>" + PAGE_TITLE + "</h1>");
		printLinks(request, response);
		out.println("</body>");
	}

	protected void printLinks(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<h2>Links</h2>");
		for (final Bookmark bookmark : bookmarkService.getBookmarks()) {
			out.println("<li>");
			out.println("<a href=\"" + bookmark.getUrl() + "\" target=\"" + target + "\">" + bookmark.getName() + "</a>");
			out.println(" ");
			out.println("[" + keywordsToString(bookmark) + "]");
			out.println("</li>");
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
