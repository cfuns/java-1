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

import de.benjaminborbe.dashboard.api.CssResourceRenderer;
import de.benjaminborbe.dashboard.api.JavascriptResourceRenderer;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchService;
import de.benjaminborbe.search.service.SearchDashboardWidget;
import de.benjaminborbe.search.util.SearchUtil;
import de.benjaminborbe.tools.html.Target;

@Singleton
public class SearchServlet extends HttpServlet {

	private static final long serialVersionUID = 7928536214812474981L;

	private final Logger logger;

	private final SearchService searchService;

	private final static String PARAMETER_SEARCH = "q";

	private static final int MAX_RESULTS = 20;

	private static final String TITLE = "Search";

	private static final Target target = Target.SELF;

	private final SearchUtil searchUtil;

	private final SearchDashboardWidget searchDashboardWidget;

	private final CssResourceRenderer cssResourceRenderer;

	private final JavascriptResourceRenderer javascriptResourceRenderer;

	@Inject
	public SearchServlet(
			final Logger logger,
			final SearchService searchService,
			final SearchUtil searchUtil,
			final SearchDashboardWidget searchDashboardWidget,
			final JavascriptResourceRenderer javascriptResourceRenderer,
			final CssResourceRenderer cssResourceRenderer) {
		this.logger = logger;
		this.searchService = searchService;
		this.searchUtil = searchUtil;
		this.searchDashboardWidget = searchDashboardWidget;
		this.javascriptResourceRenderer = javascriptResourceRenderer;
		this.cssResourceRenderer = cssResourceRenderer;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException {
		logger.debug("service");
		response.setContentType("text/html");
		printHtml(request, response);
	}

	protected void printHtml(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final PrintWriter out = response.getWriter();
		final String searchQuery = request.getParameter(PARAMETER_SEARCH);
		out.println("<html>");
		printHeader(request, response);
		printBody(request, response, searchQuery);
		out.println("</html>");
	}

	protected void printBody(final HttpServletRequest request, final HttpServletResponse response,
			final String searchQuery) throws IOException {
		printSearchForm(request, response);
		final String[] words = searchUtil.buildSearchParts(searchQuery);
		final List<SearchResult> results = searchService.search(words, MAX_RESULTS);
		printSearchResults(request, response, results);
	}

	protected void printHeader(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<head>");
		out.println("<title>" + TITLE + "</title>");
		javascriptResourceRenderer
				.render(request, response, searchDashboardWidget.getJavascriptResource(request, response));
		cssResourceRenderer.render(request, response, searchDashboardWidget.getCssResource(request, response));
		out.println("</head>");
	}

	protected void printSearchResults(final HttpServletRequest request, final HttpServletResponse response,
			final List<SearchResult> results) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<div>");
		out.println("found " + results.size() + " matches");
		out.println("</div>");
		for (final SearchResult result : results) {
			printSearchResult(request, response, result);
		}
	}

	protected void printSearchForm(final HttpServletRequest request, final HttpServletResponse response)
			throws IOException {
		searchDashboardWidget.render(request, response);
	}

	protected void printSearchResult(final HttpServletRequest request, final HttpServletResponse response,
			final SearchResult result) throws IOException {
		final PrintWriter out = response.getWriter();
		final String url = result.getUrl().toExternalForm();
		final String type = result.getType();
		final String title = result.getTitle();
		final String description = result.getDescription();
		out.println("<div class=\"searchResult\">");
		out.println("<div class=\"title\">");
		out.println("<a href=\"" + url + "\" target=\"" + target + "\">");
		out.println("[" + StringEscapeUtils.escapeHtml(type.toUpperCase()) + "] - " + StringEscapeUtils.escapeHtml(title));
		out.println("</a>");
		out.println("</div>");
		out.println("<div class=\"link\">");
		out.println("<a href=\"" + url + "\" target=\"" + target + "\">" + StringEscapeUtils.escapeHtml(url) + "</a>");
		out.println("</div>");
		out.println("<div class=\"description\">");
		out.println(StringEscapeUtils.escapeHtml(description));
		out.println("<br/>");
		out.println("</div>");
		out.println("</div>");
	}
}
