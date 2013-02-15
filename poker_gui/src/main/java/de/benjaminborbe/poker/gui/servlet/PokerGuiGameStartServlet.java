package de.benjaminborbe.poker.gui.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.poker.gui.PokerGuiConstants;
import de.benjaminborbe.poker.gui.util.PokerGuiLinkFactory;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.RedirectWidget;
import de.benjaminborbe.website.widget.BrWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;

@Singleton
public class PokerGuiGameStartServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 7727468974460815201L;

	private static final String TITLE = "Poker - Start Game";

	private final PokerService pokerService;

	private final Logger logger;

	private final PokerGuiLinkFactory pokerGuiLinkFactory;

	@Inject
	public PokerGuiGameStartServlet(
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
			final PokerService pokerService,
			final PokerGuiLinkFactory pokerGuiLinkFactory) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.pokerService = pokerService;
		this.logger = logger;
		this.pokerGuiLinkFactory = pokerGuiLinkFactory;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final PokerGameIdentifier pokerGameIdentifier = pokerService.createGameIdentifier(request.getParameter(PokerGuiConstants.PARAMETER_GAME_ID));
			pokerService.startGame(pokerGameIdentifier);
			final RedirectWidget widget = new RedirectWidget(buildRefererUrl(request));
			return widget;
		}
		catch (final PokerServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		catch (final ValidationException e) {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));
			widgets.add("start game failed!");
			widgets.add(new ValidationExceptionWidget(e));

			widgets.add(new BrWidget());
			widgets.add(pokerGuiLinkFactory.back(request));

			return widgets;
		}
	}
}
