package de.benjaminborbe.lunch.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.lunch.api.Lunch;
import de.benjaminborbe.lunch.api.LunchService;
import de.benjaminborbe.lunch.api.LunchServiceException;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.DateUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class LunchGuiServlet extends LunchGuiBaseServlet {

	private static final long serialVersionUID = 6301316368714055246L;

	private final LunchService lunchService;

	@Inject
	public LunchGuiServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final LunchService lunchService,
		final DateUtil dateUtil,
		final AuthorizationService authorizationService,
		final CacheService cacheService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, authenticationService, navigationWidget, httpContextProvider, urlUtil, dateUtil, authorizationService,
			cacheService);
		this.lunchService = lunchService;
	}

	@Override
	protected List<Lunch> getLunchs(
		final SessionIdentifier sessionIdentifier,
		final String fullname
	) throws LunchServiceException, LoginRequiredException, PermissionDeniedException {
		return new ArrayList<Lunch>(lunchService.getLunchs(sessionIdentifier, fullname));
	}

}
