package de.benjaminborbe.poker.gui.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.poker.gui.config.PokerGuiConfig;
import de.benjaminborbe.poker.gui.util.PokerGuiLinkFactory;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class PokerGuiServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 6022990232153082338L;

	private static final String TITLE = "Poker";

	private final Logger logger;

	private final PokerGuiLinkFactory pokerGuiLinkFactory;

	private final AuthenticationService authenticationService;

	private final PokerService pokerService;

	private final PokerGuiConfig pokerGuiConfig;

	@Inject
	public PokerGuiServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final Provider<HttpContext> httpContextProvider,
			final UrlUtil urlUtil,
			final CacheService cacheService,
			final PokerGuiLinkFactory pokerGuiLinkFactory,
			final PokerService pokerService,
			final PokerGuiConfig pokerGuiConfig) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.pokerGuiLinkFactory = pokerGuiLinkFactory;
		this.authenticationService = authenticationService;
		this.pokerService = pokerService;
		this.pokerGuiConfig = pokerGuiConfig;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {
		logger.trace("printContent");
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));

		final UlWidget ul = new UlWidget();
		ul.add(pokerGuiLinkFactory.gameList(request));
		ul.add(pokerGuiLinkFactory.playerList(request));
		if (pokerGuiConfig.isJsonApiEnabled()) {
			ul.add(pokerGuiLinkFactory.apiHelp(request));
		}
		widgets.add(ul);

		return widgets;
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			pokerService.expectPokerPlayerOrAdminRole(sessionIdentifier);
		}
		catch (final AuthenticationServiceException e) {
			throw new PermissionDeniedException(e);
		}
		catch (final PokerServiceException e) {
			throw new PermissionDeniedException(e);
		}
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}
}
