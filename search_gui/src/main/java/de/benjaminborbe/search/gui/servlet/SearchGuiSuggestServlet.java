package de.benjaminborbe.search.gui.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchService;
import de.benjaminborbe.search.gui.util.SearchGuiUtil;

@Singleton
public class SearchGuiSuggestServlet extends HttpServlet {

	private static final long serialVersionUID = 3708081580708674634L;

	private static final String PARAMETER_SEARCH = "term";

	private static final int MAX_RESULTS = 20;

	private static final int MIN_LENGTH = 2;

	private final Logger logger;

	private final SearchService searchService;

	private final SearchGuiUtil searchUtil;

	@Inject
	public SearchGuiSuggestServlet(final Logger logger, final SearchService searchService, final SearchGuiUtil searchUtil) {
		this.logger = logger;
		this.searchService = searchService;
		this.searchUtil = searchUtil;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		logger.debug("service");
		response.setCharacterEncoding("UTF8");
		response.setContentType("application/json");
		final String queryString = request.getParameter(PARAMETER_SEARCH);
		final PrintWriter out = response.getWriter();
		final List<SearchResult> searchResults;
		if (queryString != null && queryString.trim().length() >= MIN_LENGTH) {
			final String[] words = searchUtil.buildSearchParts(queryString);
			final SessionIdentifier sessionIdentifier = new SessionIdentifier(request);
			searchResults = searchService.search(sessionIdentifier, words, MAX_RESULTS);
			logger.debug("found " + searchResults.size() + " searchResults");
		}
		else {
			searchResults = new ArrayList<SearchResult>();
		}
		final JSONArray obj = buildJson(searchResults);
		obj.writeJSONString(out);
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
