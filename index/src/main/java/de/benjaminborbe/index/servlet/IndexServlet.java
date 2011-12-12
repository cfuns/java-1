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

import de.benjaminborbe.bookmark.api.Bookmark;
import de.benjaminborbe.bookmark.api.BookmarkService;

@Singleton
public class IndexServlet extends HttpServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String PAGE_TITLE = "BB Dashboard";

	private final Logger logger;

	private final BookmarkService bookmarkService;

	// only service are allowed to inject
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
		out.println("<link href=\"http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css\" rel=\"stylesheet\" type=\"text/css\" />");
		out.println("<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js\"></script>");
		out.println("<script src=\"http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js\"></script>");
		out.println("<link href=\"" + contextPath + "/css/style.css\" rel=\"stylesheet\" type=\"text/css\" />");
		out.println("</head>");
	}

	protected void printBody(final PrintWriter out, final String contextPath) {
		out.println("<body>");
		out.println("<h1>" + PAGE_TITLE + "</h1>");
		printSearchBox(out, contextPath);
		printLinks(out);
		out.println("</body>");
	}

	protected void printSearchBox(final PrintWriter out, final String contextPath) {
		final String searchUrl = contextPath + "/bookmarksearch";
		out.println("<form method=\"POST\" target=\"_blank\" action=\"" + contextPath + "/go\">");
		out.println("<input name=\"q\" id=\"searchBox\" type=\"text\" />");
		out.println("<input type=\"submit\" value=\"search\" />");
		out.println("</form>");
		out.println("<script language=\"javascript\">");
		out.println("$(document).ready(function() {");
		out.println("$('input#searchBox').autocomplete({");
		out.println("source: '" + searchUrl + "',");
		out.println("method: 'POST',");
		out.println("minLength: 1,");
		out.println("});");
		out.println("});");
		out.println("</script>");
	}

	//
	// $("input[name=receiver]").autocomplete({
	// source: "http://jquery.bassistance.de/autocomplete/demo/search.php"
	// });

	protected void printLinks(final PrintWriter out) {
		out.println("<h2>Links</h2>");
		for (final Bookmark bookmark : bookmarkService.getBookmarks()) {
			out.println("<li>");
			out.println("<a href=\"" + bookmark.getUrl() + "\" target=\"_blank\">" + bookmark.getName() + "</a>");
			out.println("</li>");
		}
	}

}
