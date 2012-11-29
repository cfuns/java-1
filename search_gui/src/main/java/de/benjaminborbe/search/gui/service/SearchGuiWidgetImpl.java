package de.benjaminborbe.search.gui.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchService;
import de.benjaminborbe.search.api.SearchSpecial;
import de.benjaminborbe.search.api.SearchWidget;
import de.benjaminborbe.tools.html.Target;
import de.benjaminborbe.tools.util.SearchUtil;
import de.benjaminborbe.tools.util.StringUtil;
import de.benjaminborbe.website.util.ExceptionWidget;

@Singleton
public class SearchGuiWidgetImpl implements SearchWidget {

	private final Logger logger;

	private final SearchService searchService;

	private final static String PARAMETER_SEARCH = "q";

	private static final int MAX_RESULTS = 20;

	private static final Target target = Target.BLANK;

	private final SearchUtil searchUtil;

	private final SearchGuiDashboardWidget searchDashboardWidget;

	private final SearchGuiSpecialSearchFactory searchGuiSpecialSearchFactory;

	private final AuthenticationService authenticationService;

	private final StringUtil stringUtil;

	@Inject
	public SearchGuiWidgetImpl(
			final Logger logger,
			final SearchUtil searchUtil,
			final SearchService searchService,
			final SearchGuiDashboardWidget searchDashboardWidget,
			final SearchGuiSpecialSearchFactory searchGuiSpecialSearchFactory,
			final AuthenticationService authenticationService,
			final StringUtil stringUtil) {
		this.logger = logger;
		this.searchUtil = searchUtil;
		this.searchService = searchService;
		this.searchDashboardWidget = searchDashboardWidget;
		this.searchGuiSpecialSearchFactory = searchGuiSpecialSearchFactory;
		this.authenticationService = authenticationService;
		this.stringUtil = stringUtil;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			logger.trace("render");
			final String searchQuery = request.getParameter(PARAMETER_SEARCH);
			logger.trace("searchQuery: " + searchQuery);
			final SearchSpecial searchGuiSpecialSearch = searchGuiSpecialSearchFactory.findSpecial(searchQuery);
			if (searchGuiSpecialSearch != null) {
				logger.trace("found special search");
				searchGuiSpecialSearch.render(request, response, context);
				return;
			}
			else {
				logger.trace("found no special search");
			}

			printSearchForm(request, response, context);
			final String[] words = searchUtil.buildSearchParts(searchQuery);
			SessionIdentifier sessionIdentifier;
			sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final List<SearchResult> results = searchService.search(sessionIdentifier, searchQuery, words, MAX_RESULTS);
			printSearchResults(request, response, results);
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			exceptionWidget.render(request, response, context);
		}
	}

	@Override
	public List<JavascriptResource> getJavascriptResource(final HttpServletRequest request, final HttpServletResponse response) {
		return searchDashboardWidget.getJavascriptResource(request, response);
	}

	@Override
	public List<CssResource> getCssResource(final HttpServletRequest request, final HttpServletResponse response) {
		return searchDashboardWidget.getCssResource(request, response);
	}

	protected void printSearchResults(final HttpServletRequest request, final HttpServletResponse response, final List<SearchResult> results) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<div>");
		out.println("found " + results.size() + " matches");
		out.println("</div>");
		for (final SearchResult result : results) {
			printSearchResult(request, response, result);
		}
	}

	protected void printSearchForm(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		searchDashboardWidget.render(request, response, context);
	}

	protected void printSearchResult(final HttpServletRequest request, final HttpServletResponse response, final SearchResult result) throws IOException {
		final PrintWriter out = response.getWriter();
		final URL url = buildUrl(request, result.getUrl());
		final String urlString = url.toExternalForm();
		final String type = result.getType();
		final String title = result.getTitle();
		final String description = result.getDescription();
		out.println("<div class=\"searchResult\">");
		out.println("<div class=\"title\">");
		out.println("<a href=\"" + url + "\" target=\"" + target + "\">");
		out.println("[" + StringEscapeUtils.escapeHtml(type.toUpperCase()) + "] - " + StringEscapeUtils.escapeHtml(stringUtil.shorten(title, 100)));
		out.println("</a>");
		out.println("</div>");
		out.println("<div class=\"link\">");
		out.println("<a href=\"" + urlString + "\" target=\"" + target + "\">" + StringEscapeUtils.escapeHtml(stringUtil.shorten(urlString, 100)) + "</a>");
		out.println("</div>");
		out.println("<div class=\"description\">");
		out.println(StringEscapeUtils.escapeHtml(stringUtil.shorten(description, 400)));
		out.println("<br/>");
		out.println("</div>");
		out.println("</div>");
	}

	protected URL buildUrl(final HttpServletRequest request, final String url) throws MalformedURLException {
		if (url.indexOf("/") == 0) {
			return new URL(request.getScheme() + "://" + request.getServerName() + request.getContextPath() + url);
		}
		else {
			return new URL(url);
		}
	}
}
