package de.benjaminborbe.index.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.index.dao.Bookmark;
import de.benjaminborbe.index.service.BookmarkService;

@Singleton
public class IndexServlet extends HttpServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String PAGE_TITLE = "BB Dashboard";

	private final Logger logger;

	private final BookmarkService bookmarkService;

	@Inject
	public IndexServlet(final Logger logger, final BookmarkService bookmarkService) {
		this.logger = logger;
		this.bookmarkService = bookmarkService;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException {
		logger.debug("service");
		response.setContentType("text/html");
		final PrintWriter out = response.getWriter();
		printHtml(out);
	}

	protected void printHtml(final PrintWriter out) {
		out.println("<html>");
		printHead(out);
		printBody(out);
		out.println("</html>");
	}

	protected void printHead(final PrintWriter out) {
		out.println("<head>");
		out.println("<title>" + PAGE_TITLE + "</title>");
		out.println("</head>");
	}

	protected void printBody(final PrintWriter out) {
		out.println("<body>");
		out.println("<h1>" + PAGE_TITLE + "</h1>");
		printLinks(out);
		out.println("</body>");
	}

	protected void printLinks(final PrintWriter out) {
		out.println("<h2>Links</h2>");
		for (final Bookmark bookmark : bookmarkService.getBookmarks()) {
			out.println("<li>");
			out.println("<a href=\"" + bookmark.getUrl() + "\" target=\"_blank\">" + bookmark.getName() + "</a>");
			out.println("</li>");
		}
	}

}
