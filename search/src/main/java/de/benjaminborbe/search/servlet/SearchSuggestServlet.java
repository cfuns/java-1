package de.benjaminborbe.search.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchService;
import de.benjaminborbe.search.util.SearchUtil;

@Singleton
public class SearchSuggestServlet extends HttpServlet {

	private static final long serialVersionUID = 3708081580708674634L;

	private static final String PARAMETER_SEARCH = "term";

	private static final int MAX_RESULTS = 20;

	private final Logger logger;

	private final SearchService searchService;

	private final SearchUtil searchUtil;

	@Inject
	public SearchSuggestServlet(final Logger logger, final SearchService searchService, final SearchUtil searchUtil) {
		this.logger = logger;
		this.searchService = searchService;
		this.searchUtil = searchUtil;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		logger.debug("service");
		response.setContentType("application/json");
		final PrintWriter out = response.getWriter();
		final String queryString = request.getParameter(PARAMETER_SEARCH);
		final String[] words = searchUtil.buildSearchParts(queryString);
		final List<SearchResult> searchResults = searchService.search(words, MAX_RESULTS);
		logger.debug("found " + searchResults.size() + " searchResults");
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
