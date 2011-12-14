package de.benjaminborbe.search.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchService;

@Singleton
public class SearchServlet extends HttpServlet {

	private static final long serialVersionUID = 7928536214812474981L;

	private final Logger logger;

	private final SearchService searchService;

	private final static String PARAMETER_SEARCH = "q";

	private static final int MAX_RESULTS = 20;

	private static final String TITLE = "Search";

	@Inject
	public SearchServlet(final Logger logger, final SearchService searchService) {
		this.logger = logger;
		this.searchService = searchService;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException {
		logger.debug("service");
		response.setContentType("text/html");
		final PrintWriter out = response.getWriter();
		final String searchQuery = request.getParameter(PARAMETER_SEARCH);
		out.println("<html>");
		printHeader(out);
		printBody(out, searchQuery);
		printFooter(out);
		out.println("</html>");
	}

	protected void printBody(final PrintWriter out, final String searchQuery) {
		printSearchForm(out, searchQuery);
		final List<SearchResult> results = searchService.search(searchQuery, MAX_RESULTS);
		printSearchResults(out, results);
	}

	protected void printHeader(final PrintWriter out) {
		out.println("<head>");
		out.println("<title>" + TITLE + "</title>");
		out.println("</head>");
	}

	protected void printFooter(final PrintWriter out) {
	}

	protected void printSearchResults(final PrintWriter out, final List<SearchResult> results) {
		out.println("<div>");
		out.println("found " + results.size() + " matches");
		out.println("</div>");
		for (final SearchResult result : results) {
			printSearchResult(out, result);
		}
	}

	protected void printSearchForm(final PrintWriter out, final String searchQuery) {
		out.println("<form method=\"GET\" action=\"\">");
		out.println("<input type=\"text\" name=\"q\" value=\""
				+ (searchQuery != null ? StringEscapeUtils.escapeHtml(searchQuery) : "") + "\" />");
		out.println("<input type=\"submit\" value=\"search\" />");
		out.println("</form>");
	}

	protected void printSearchResult(final PrintWriter out, final SearchResult result) {
		final String url = result.getUrl().toExternalForm();
		final String type = result.getType();
		final String title = result.getTitle();
		final String description = result.getDescription();
		out.println("<div>");
		out.println("<a href=\"" + url + "\" target=\"_blank\">");
		out.println("[" + StringEscapeUtils.escapeHtml(type.toUpperCase()) + "] - " + StringEscapeUtils.escapeHtml(title));
		out.println("</a>");
		out.println("<br/>");
		out.println("<a href=\"" + url + "\" target=\"_blank\">" + StringEscapeUtils.escapeHtml(url) + "</a>");
		out.println("<br/>");
		out.println(StringEscapeUtils.escapeHtml(description));
		out.println("<br/>");
		out.println("</div>");
	}
}
