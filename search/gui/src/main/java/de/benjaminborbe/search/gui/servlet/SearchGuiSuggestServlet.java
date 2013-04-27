package de.benjaminborbe.search.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchService;
import de.benjaminborbe.search.api.SearchServiceException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.json.JSONArray;
import de.benjaminborbe.tools.json.JSONArraySimple;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteWidgetServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.HtmlContentWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class SearchGuiSuggestServlet extends WebsiteWidgetServlet {

	private static final long serialVersionUID = 3708081580708674634L;

	private static final String PARAMETER_SEARCH = "term";

	private static final int MAX_RESULTS = 20;

	private static final int MIN_LENGTH = 2;

	private final Logger logger;

	private final SearchService searchService;

	private final AuthenticationService authenticationService;

	@Inject
	public SearchGuiSuggestServlet(
		final Logger logger,
		final UrlUtil urlUtil,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final SearchService searchService,
		final Provider<HttpContext> httpContextProvider,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService
	) {
		super(logger, urlUtil, calendarUtil, timeZoneUtil, httpContextProvider, authenticationService, authorizationService);
		this.logger = logger;
		this.searchService = searchService;
		this.authenticationService = authenticationService;
	}

	protected JSONArray buildJson(final List<SearchResult> searchResults) {
		final JSONArray result = new JSONArraySimple();
		for (final SearchResult searchResult : searchResults) {
			// final JSONObject bookmarkJson = new JSONObject();
			// bookmarkJson.put("id", new Long(bookmark.getId()));
			// bookmarkJson.put("text", bookmark.getName());
			// result.add(bookmarkJson);
			result.add(searchResult.getTitle());
		}
		return result;
	}

	@Override
	public Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			logger.trace("service");
			response.setCharacterEncoding("UTF8");
			response.setContentType("application/json");
			final String queryString = request.getParameter(PARAMETER_SEARCH);
			final List<SearchResult> searchResults;
			if (queryString != null && queryString.trim().length() >= MIN_LENGTH) {
				final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
				searchResults = searchService.search(sessionIdentifier, queryString, MAX_RESULTS);
				logger.trace("found " + searchResults.size() + " searchResults");
			} else {
				searchResults = new ArrayList<>();
			}
			final JSONArray obj = buildJson(searchResults);
			final StringWriter sw = new StringWriter();
			obj.writeJSONString(sw);

			return new HtmlContentWidget(sw.toString());
		} catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			response.setContentType("text/plain");
			return new ExceptionWidget(e);
		} catch (final SearchServiceException e) {
			logger.debug(e.getClass().getName(), e);
			response.setContentType("text/plain");
			return new ExceptionWidget(e);
		}
	}

	@Override
	public boolean isLoginRequired() {
		return true;
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

}
