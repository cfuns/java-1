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
		final String contextPath = request.getContextPath();
		final String searchQuery = request.getParameter(PARAMETER_SEARCH);
		out.println("<html>");
		printHeader(out, contextPath);
		printBody(out, contextPath, searchQuery);
		printFooter(out);
		out.println("</html>");
	}

	protected void printBody(final PrintWriter out, final String contextPath, final String searchQuery) {
		printSearchForm(out, contextPath, searchQuery);
		final List<SearchResult> results = searchService.search(searchQuery, MAX_RESULTS);
		printSearchResults(out, results);
	}

	protected void printHeader(final PrintWriter out, final String contextPath) {
		out.println("<head>");
		out.println("<title>" + TITLE + "</title>");
		out.println("<link href=\"http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css\" rel=\"stylesheet\" type=\"text/css\" />");
		out.println("<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js\"></script>");
		out.println("<script src=\"http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js\"></script>");
		out.println("<link href=\"" + contextPath + "/search/css/style.css\" rel=\"stylesheet\" type=\"text/css\" />");
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

	protected void printSearchForm(final PrintWriter out, final String contextPath, final String searchQuery) {
		final String searchSuggestUrl = contextPath + "/search/suggest";
		out.println("<form method=\"GET\" action=\"\">");
		out.println("<input name=\"q\" id=\"searchBox\" type=\"text\""
				+ (searchQuery != null ? StringEscapeUtils.escapeHtml(searchQuery) : "") + "\" />");
		out.println("<input type=\"submit\" value=\"search\" />");
		out.println("</form>");
		out.println("<script language=\"javascript\">");
		out.println("$(document).ready(function() {");
		out.println("$('input#searchBox').autocomplete({");
		out.println("source: '" + searchSuggestUrl + "',");
		out.println("method: 'POST',");
		out.println("minLength: 1,");
		out.println("});");
		out.println("});");
		out.println("</script>");
	}

	protected void printSearchResult(final PrintWriter out, final SearchResult result) {
		final String url = result.getUrl().toExternalForm();
		final String type = result.getType();
		final String title = result.getTitle();
		final String description = result.getDescription();
		out.println("<div class=\"searchResult\">");
		out.println("<div class=\"title\">");
		out.println("<a href=\"" + url + "\" target=\"_blank\">");
		out.println("[" + StringEscapeUtils.escapeHtml(type.toUpperCase()) + "] - " + StringEscapeUtils.escapeHtml(title));
		out.println("</a>");
		out.println("</div>");
		out.println("<div class=\"link\">");
		out.println("<a href=\"" + url + "\" target=\"_blank\">" + StringEscapeUtils.escapeHtml(url) + "</a>");
		out.println("</div>");
		out.println("<div class=\"description\">");
		out.println(StringEscapeUtils.escapeHtml(description));
		out.println("<br/>");
		out.println("</div>");
		out.println("</div>");
	}
}
