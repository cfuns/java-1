package de.benjaminborbe.search.gui.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

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
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteJsonServlet;

@Singleton
public class SearchGuiJsonServlet extends WebsiteJsonServlet {

	private static final long serialVersionUID = 8865908885832843737L;

	private static final int MAXRESULTS = 10;

	private final Logger logger;

	private final SearchService searchService;

	private final AuthenticationService authenticationService;

	private final SearchGuiConfig searchGuiConfig;

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
			final SearchGuiConfig searchGuiConfig) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.logger = logger;
		this.searchService = searchService;
		this.authenticationService = authenticationService;
		this.searchGuiConfig = searchGuiConfig;
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException, LoginRequiredException {
		final String token = request.getParameter(SearchGuiConstants.PARAMETER_AUTH_TOKEN);
		logger.debug("doCheckPermission");
		expectAuthToken(token);
	}

	private void expectAuthToken(final String token) throws PermissionDeniedException {
		if (!searchGuiConfig.getAuthToken().equals(token)) {
			throw new PermissionDeniedException("invalid token");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException, LoginRequiredException {
		try {
			logger.debug("doService");
			final String token = request.getParameter(SearchGuiConstants.PARAMETER_AUTH_TOKEN);
			final String searchQuery = request.getParameter(SearchGuiConstants.PARAMETER_SEARCH);
			if (token != null && searchQuery != null) {
				final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
				final List<SearchResult> results = searchService.search(sessionIdentifier, searchQuery, MAXRESULTS);

				final JSONObject object = new JSONObject();
				final JSONArray array = new JSONArray();
				for (final SearchResult result : results) {
					final JSONObject resultObject = new JSONObject();
					resultObject.put("description", result.getDescription());
					resultObject.put("matchcounter", result.getMatchCounter());
					resultObject.put("title", result.getTitle());
					resultObject.put("type", result.getType());
					resultObject.put("url", result.getUrl());
					array.add(resultObject);
				}
				object.put("results", array);
				object.put("searchQuery", searchQuery);
				object.put("maxResults", MAXRESULTS);
				printJson(response, object);
			}
			else {
				printError(response, "parameter required: " + SearchGuiConstants.PARAMETER_AUTH_TOKEN + " and " + SearchGuiConstants.PARAMETER_SEARCH);
			}
		}
		catch (final SearchServiceException e) {
			printException(response, e);
		}
		catch (final AuthenticationServiceException e) {
			printException(response, e);
		}
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
