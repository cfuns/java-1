package de.benjaminborbe.search.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchService;
import de.benjaminborbe.search.api.SearchServiceException;
import de.benjaminborbe.search.gui.SearchGuiConstants;
import de.benjaminborbe.search.gui.config.SearchGuiConfig;
import de.benjaminborbe.search.gui.util.SearchGuiShortener;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.json.JSONArray;
import de.benjaminborbe.tools.json.JSONArraySimple;
import de.benjaminborbe.tools.json.JSONObject;
import de.benjaminborbe.tools.json.JSONObjectSimple;
import de.benjaminborbe.tools.search.SearchUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.WebsiteConstants;
import de.benjaminborbe.website.servlet.WebsiteJsonServlet;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

@Singleton
public class SearchGuiJsonServlet extends WebsiteJsonServlet {

	private static final long serialVersionUID = 8865908885832843737L;

	private static final int MAXRESULTS = 10;

	private final Logger logger;

	private final SearchService searchService;

	private final AuthenticationService authenticationService;

	private final SearchGuiConfig searchGuiConfig;

	private final SearchGuiShortener searchGuiShortener;

	private final SearchUtil searchUtil;

	private final CalendarUtil calendarUtil;

	private final TimeZoneUtil timeZoneUtil;

	private final ParseUtil parseUtil;

	@Inject
	public SearchGuiJsonServlet(
		final Logger logger,
		final UrlUtil urlUtil,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final Provider<HttpContext> httpContextProvider,
		final SearchService searchService,
		final SearchGuiConfig searchGuiConfig,
		final SearchGuiShortener searchGuiShortener,
		final SearchUtil searchUtil,
		final ParseUtil parseUtil
	) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.searchService = searchService;
		this.authenticationService = authenticationService;
		this.searchGuiConfig = searchGuiConfig;
		this.searchGuiShortener = searchGuiShortener;
		this.searchUtil = searchUtil;
		this.parseUtil = parseUtil;
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request) throws ServletException, IOException,
		PermissionDeniedException, LoginRequiredException {
		final String token = request.getParameter(SearchGuiConstants.PARAMETER_AUTH_TOKEN);
		logger.debug("doCheckPermission");
		expectAuthToken(token);
	}

	private void expectAuthToken(final String token) throws PermissionDeniedException {
		if (searchGuiConfig.getAuthToken() == null || token == null || !searchGuiConfig.getAuthToken().equals(token)) {
			throw new PermissionDeniedException("invalid token");
		}
	}

	@Override
	protected void doService(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final HttpContext context
	) throws ServletException, IOException,
		PermissionDeniedException, LoginRequiredException {
		try {
			logger.debug("doService");
			final String token = request.getParameter(SearchGuiConstants.PARAMETER_AUTH_TOKEN);
			final String searchQuery = request.getParameter(SearchGuiConstants.PARAMETER_SEARCH);
			if (token != null && searchQuery != null) {
				final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
				final List<SearchResult> results = searchService.search(sessionIdentifier, searchQuery, MAXRESULTS);
				final List<String> words = searchUtil.buildSearchParts(searchQuery);
				final JSONObject object = new JSONObjectSimple();
				final JSONArray array = new JSONArraySimple();
				for (final SearchResult result : results) {
					final JSONObject resultObject = new JSONObjectSimple();
					resultObject.put("description", searchGuiShortener.shortenDescription(result.getDescription(), words));
					resultObject.put("matchcounter", result.getMatchCounter());
					resultObject.put("title", searchGuiShortener.shortenTitle(result.getTitle()));
					resultObject.put("type", result.getType());
					resultObject.put("url", searchGuiShortener.shortenUrl(result.getUrl()));
					array.add(resultObject);
				}
				object.put("results", array);
				object.put("searchQuery", searchQuery);
				object.put("maxResults", MAXRESULTS);
				object.put("matches", String.valueOf(results.size()));
				final long now = getNowAsLong();
				final long startTime = parseUtil.parseLong(context.getData().get(WebsiteConstants.START_TIME), now);
				final long duration = (now - startTime);
				object.put("duration", String.valueOf(duration));

				printJson(response, object);
			} else {
				printError(response, "parameter required: " + SearchGuiConstants.PARAMETER_AUTH_TOKEN + " and " + SearchGuiConstants.PARAMETER_SEARCH);
			}
		} catch (final SearchServiceException | AuthenticationServiceException e) {
			printException(response, e);
		}
	}

	private long getNowAsLong() {
		final TimeZone timeZone = timeZoneUtil.getUTCTimeZone();
		final Calendar now = calendarUtil.now(timeZone);
		return now.getTimeInMillis();
	}

	@Override
	public boolean isLoginRequired() {
		return false;
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}
}
