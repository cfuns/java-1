package de.benjaminborbe.search.gui.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchService;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.SearchUtil;
import de.benjaminborbe.website.servlet.WebsiteServlet;
import de.benjaminborbe.website.util.ExceptionWidget;

@Singleton
public class SearchGuiSuggestServlet extends WebsiteServlet {

	private static final long serialVersionUID = 3708081580708674634L;

	private static final String PARAMETER_SEARCH = "term";

	private static final int MAX_RESULTS = 20;

	private static final int MIN_LENGTH = 2;

	private final Logger logger;

	private final SearchService searchService;

	private final SearchUtil searchUtil;

	private final AuthenticationService authenticationService;

	@Inject
	public SearchGuiSuggestServlet(
			final Logger logger,
			final SearchService searchService,
			final SearchUtil searchUtil,
			final AuthenticationService authenticationService,
			final UrlUtil urlUtil,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider) {
		super(logger, urlUtil, authenticationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.logger = logger;
		this.searchService = searchService;
		this.searchUtil = searchUtil;
		this.authenticationService = authenticationService;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException {
		logger.trace("service");
		response.setCharacterEncoding("UTF8");
		try {
			response.setContentType("application/json");
			final String queryString = request.getParameter(PARAMETER_SEARCH);
			final PrintWriter out = response.getWriter();
			final List<SearchResult> searchResults;
			if (queryString != null && queryString.trim().length() >= MIN_LENGTH) {
				final String[] words = searchUtil.buildSearchParts(queryString);
				final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
				searchResults = searchService.search(sessionIdentifier, queryString, words, MAX_RESULTS);
				logger.trace("found " + searchResults.size() + " searchResults");
			}
			else {
				searchResults = new ArrayList<SearchResult>();
			}
			final JSONArray obj = buildJson(searchResults);
			obj.writeJSONString(out);
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			response.setContentType("text/plain");
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			exceptionWidget.render(request, response, context);
		}
	}

	@SuppressWarnings("unchecked")
	protected JSONArray buildJson(final List<SearchResult> searchResults) {
		final JSONArray result = new JSONArray();
		for (final SearchResult searchResult : searchResults) {
			// final JSONObject bookmarkJson = new JSONObject();
			// bookmarkJson.put("id", new Long(bookmark.getId()));
			// bookmarkJson.put("text", bookmark.getName());
			// result.add(bookmarkJson);
			result.add(searchResult.getTitle());
		}
		return result;
	}
}
