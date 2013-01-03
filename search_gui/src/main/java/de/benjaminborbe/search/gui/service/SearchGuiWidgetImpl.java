package de.benjaminborbe.search.gui.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

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
import de.benjaminborbe.search.api.SearchServiceException;
import de.benjaminborbe.search.api.SearchSpecial;
import de.benjaminborbe.search.api.SearchWidget;
import de.benjaminborbe.search.gui.SearchGuiConstants;
import de.benjaminborbe.search.gui.util.SearchGuiTermHighlighter;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.html.Target;
import de.benjaminborbe.tools.search.SearchUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.StringUtil;
import de.benjaminborbe.website.servlet.WebsiteConstants;
import de.benjaminborbe.website.util.ExceptionWidget;

@Singleton
public class SearchGuiWidgetImpl implements SearchWidget {

	private final Logger logger;

	private final SearchService searchService;

	private final static String PARAMETER_SEARCH = "q";

	private static final int MAX_RESULTS = 10;

	private static final Target target = Target.BLANK;

	private final SearchUtil searchUtil;

	private final SearchGuiDashboardWidget searchDashboardWidget;

	private final SearchGuiSpecialSearchFactory searchGuiSpecialSearchFactory;

	private final AuthenticationService authenticationService;

	private final StringUtil stringUtil;

	private final SearchGuiTermHighlighter searchGuiTermHighlighter;

	private final TimeZoneUtil timeZoneUtil;

	private final CalendarUtil calendarUtil;

	private final ParseUtil parseUtil;

	private final UrlUtil urlUtil;

	@Inject
	public SearchGuiWidgetImpl(
			final Logger logger,
			final SearchUtil searchUtil,
			final SearchService searchService,
			final SearchGuiDashboardWidget searchDashboardWidget,
			final SearchGuiSpecialSearchFactory searchGuiSpecialSearchFactory,
			final AuthenticationService authenticationService,
			final StringUtil stringUtil,
			final SearchGuiTermHighlighter searchGuiTermHighlighter,
			final ParseUtil parseUtil,
			final UrlUtil urlUtil,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil) {
		this.logger = logger;
		this.searchUtil = searchUtil;
		this.searchService = searchService;
		this.searchDashboardWidget = searchDashboardWidget;
		this.searchGuiSpecialSearchFactory = searchGuiSpecialSearchFactory;
		this.authenticationService = authenticationService;
		this.stringUtil = stringUtil;
		this.searchGuiTermHighlighter = searchGuiTermHighlighter;
		this.parseUtil = parseUtil;
		this.urlUtil = urlUtil;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			final String searchQuery = trim(request.getParameter(PARAMETER_SEARCH));
			logger.trace("searchQuery: " + searchQuery);

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			searchService.createHistory(sessionIdentifier, searchQuery);

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

			final List<String> words = searchUtil.buildSearchParts(searchQuery);
			if (words.size() > 0) {
				final List<SearchResult> results = searchService.search(sessionIdentifier, searchQuery, MAX_RESULTS);
				printSearchResults(request, response, context, results, words);
			}
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			exceptionWidget.render(request, response, context);
		}
		catch (final SearchServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			exceptionWidget.render(request, response, context);
		}
	}

	private String trim(final String parameter) {
		return parameter != null ? parameter.trim() : "";
	}

	@Override
	public List<JavascriptResource> getJavascriptResource(final HttpServletRequest request, final HttpServletResponse response) {
		return searchDashboardWidget.getJavascriptResource(request, response);
	}

	@Override
	public List<CssResource> getCssResource(final HttpServletRequest request, final HttpServletResponse response) {
		return searchDashboardWidget.getCssResource(request, response);
	}

	protected void printSearchResults(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context, final List<SearchResult> results,
			final List<String> words) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<div class=\"resultcounter\">");
		final long now = getNowAsLong();
		final long startTime = parseUtil.parseLong(context.getData().get(WebsiteConstants.START_TIME), now);
		final long duration = (now - startTime);
		out.println("found " + results.size() + " matches in " + duration + " ms");
		out.println("</div>");
		for (final SearchResult result : results) {
			printSearchResult(request, response, result, words);
		}
	}

	private long getNowAsLong() {
		final TimeZone timeZone = timeZoneUtil.getUTCTimeZone();
		final Calendar now = calendarUtil.now(timeZone);
		return now.getTimeInMillis();
	}

	protected void printSearchForm(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		searchDashboardWidget.render(request, response, context);
	}

	protected void printSearchResult(final HttpServletRequest request, final HttpServletResponse response, final SearchResult result, final List<String> words) throws IOException {
		try {
			final PrintWriter out = response.getWriter();
			final URL url = urlUtil.buildUrl(request, result.getUrl());
			final String urlString = url.toExternalForm();
			final String type = result.getType();
			final String title = result.getTitle();
			final String description = result.getDescription() != null && result.getDescription().length() > 0 ? result.getDescription() : "-";
			out.println("<div class=\"searchResult\">");
			out.println("<div class=\"title\">");
			out.println("<a href=\"" + url + "\" target=\"" + target + "\">");
			out.println("[" + StringEscapeUtils.escapeHtml(type.toUpperCase()) + "] - "
					+ searchGuiTermHighlighter.highlightSearchTerms(StringEscapeUtils.escapeHtml(stringUtil.shortenDots(title, SearchGuiConstants.TITLE_MAX_CHARS)), words));
			out.println("</a>");
			if (result.getMatchCounter() > 0 && result.getMatchCounter() < Integer.MAX_VALUE) {
				out.println("<span class=\"matchCounter\">( hitrate: " + result.getMatchCounter() + " )</span>");
			}
			out.println("</div>");
			out.println("<div class=\"link\">");
			out.println("<a href=\"" + urlString + "\" target=\"" + target + "\">"
					+ searchGuiTermHighlighter.highlightSearchTerms(StringEscapeUtils.escapeHtml(stringUtil.shortenDots(urlString, SearchGuiConstants.URL_MAX_CHARS)), words) + "</a>");
			out.println("</div>");
			out.println("<div class=\"description\">");
			out.println(searchGuiTermHighlighter.highlightSearchTerms(StringEscapeUtils.escapeHtml(buildDescriptionPreview(description, words)), words));
			out.println("<br/>");
			out.println("</div>");
			out.println("</div>");
		}
		catch (final MalformedURLException e) {
			logger.debug("illegal type: " + result.getType() + " url: " + result.getUrl());
			// nop
		}
	}

	private String buildDescriptionPreview(final String content, final List<String> words) {
		if (content.length() > SearchGuiConstants.CONTENT_MAX_CHARS) {
			Integer firstMatch = null;
			for (final String word : words) {
				final int pos = content.indexOf(word);
				if (firstMatch == null || pos != -1 && firstMatch > pos) {
					firstMatch = pos;
				}
			}
			if (firstMatch != null && firstMatch > 0) {
				return "..." + stringUtil.shortenDots(content.substring(Math.max(0, firstMatch - 20)), SearchGuiConstants.CONTENT_MAX_CHARS);
			}
		}
		return stringUtil.shortenDots(content, SearchGuiConstants.CONTENT_MAX_CHARS);
	}

}
