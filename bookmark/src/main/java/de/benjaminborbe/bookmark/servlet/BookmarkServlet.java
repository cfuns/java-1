package de.benjaminborbe.bookmark.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.bookmark.api.Bookmark;
import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.tools.html.Target;

@Singleton
public class BookmarkServlet extends HttpServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String PAGE_TITLE = "Bookmarks";

	private static final Target target = Target.BLANK;

	private final Logger logger;

	private final BookmarkService bookmarkService;

	@Inject
	public BookmarkServlet(final Logger logger, final BookmarkService bookmarkService) {
		this.logger = logger;
		this.bookmarkService = bookmarkService;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		logger.debug("service");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		final PrintWriter out = response.getWriter();
		final String contextPath = request.getContextPath();
		printHtml(out, contextPath);
	}

	protected void printHtml(final PrintWriter out, final String contextPath) {
		out.println("<html>");
		printHead(out, contextPath);
		printBody(out, contextPath);
		out.println("</html>");
	}

	protected void printHead(final PrintWriter out, final String contextPath) {
		out.println("<head>");
		out.println("<title>" + PAGE_TITLE + "</title>");
		out.println("</head>");
	}

	protected void printBody(final PrintWriter out, final String contextPath) {
		out.println("<body>");
		out.println("<h1>" + PAGE_TITLE + "</h1>");
		printLinks(out);
		out.println("</body>");
	}

	protected void printLinks(final PrintWriter out) {
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
}
