package de.benjaminborbe.poker.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.poker.api.PokerGame;
import de.benjaminborbe.poker.api.PokerPlayer;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.poker.gui.PokerGuiConstants;
import de.benjaminborbe.poker.gui.util.PokerGuiLinkFactory;
import de.benjaminborbe.poker.gui.util.UserIdentifierComparator;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ComparatorUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.SingleTagWidget;
import de.benjaminborbe.website.util.UlWidget;
import de.benjaminborbe.website.widget.BrWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Singleton
public class PokerGuiPlayerViewServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Poker - Player - View";

	private final PokerService pokerService;

	private final PokerGuiLinkFactory pokerGuiLinkFactory;

	private final AuthenticationService authenticationService;

	private final ComparatorUtil comparatorUtil;

	@Inject
	public PokerGuiPlayerViewServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final CacheService cacheService,
		final PokerService pokerService,
		final PokerGuiLinkFactory pokerGuiLinkFactory,
		final ComparatorUtil comparatorUtil
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.pokerService = pokerService;
		this.pokerGuiLinkFactory = pokerGuiLinkFactory;
		this.authenticationService = authenticationService;
		this.comparatorUtil = comparatorUtil;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final PokerPlayerIdentifier playerIdentifier = pokerService.createPlayerIdentifier(request.getParameter(PokerGuiConstants.PARAMETER_PLAYER_ID));
			final PokerPlayer player = pokerService.getPlayer(playerIdentifier);
			final List<UserIdentifier> owners = comparatorUtil.sort(player.getOwners(), new UserIdentifierComparator());

			widgets.add("Name: " + player.getName());
			widgets.add(new BrWidget());

			widgets.add("Credits: " + player.getAmount() + " ");
			widgets.add(pokerGuiLinkFactory.createHistoryLink(request, "history", playerIdentifier));
			widgets.add(new BrWidget());

			widgets.add("Score: " + toNumberString(player.getScore()) + " ");
			widgets.add(new BrWidget());

			if (pokerService.hasPokerAdminPermission(sessionIdentifier) || owners.contains(authenticationService.getCurrentUser(sessionIdentifier))) {
				widgets.add("ID: " + player.getId());
				widgets.add(new BrWidget());
				widgets.add("Token: " + player.getToken());
				widgets.add(new BrWidget());
			}
			widgets.add("Owners: ");
			for (final UserIdentifier uid : owners) {
				widgets.add(uid.getId());
				widgets.add(" ");
			}
			widgets.add(new BrWidget());

			if (player.getGame() == null) {
				widgets.add("Game: none");
				widgets.add(new BrWidget());

				final Collection<PokerGame> games = pokerService.getGamesNotRunning();
				if (games.isEmpty()) {
					widgets.add("no join able game found");
				} else {
					final UlWidget ul = new UlWidget();
					for (final PokerGame game : games) {
						final ListWidget list = new ListWidget();
						list.add(pokerGuiLinkFactory.gameView(request, game.getId(), game.getName()));
						list.add(" ");
						if (pokerService.hasPokerAdminPermission(sessionIdentifier)) {
							list.add(pokerGuiLinkFactory.gameJoin(request, game.getId(), playerIdentifier));
							list.add(" ");
						}
						ul.add(list);
					}
					widgets.add(ul);
				}

			} else {
				final PokerGame game = pokerService.getGame(player.getGame());
				widgets.add("Game: ");
				widgets.add(pokerGuiLinkFactory.gameView(request, game.getId(), game.getName()));
				widgets.add(new BrWidget());
				if (pokerService.hasPokerAdminPermission(sessionIdentifier)) {
					if (!Boolean.TRUE.equals(game.getRunning())) {
						widgets.add(pokerGuiLinkFactory.gameLeave(request, player.getGame(), playerIdentifier));
					}
					widgets.add(new BrWidget());
				}
			}

			widgets.add(new SingleTagWidget("hr"));
			widgets.add(pokerGuiLinkFactory.gameList(request));
			widgets.add(" ");
			widgets.add(pokerGuiLinkFactory.playerList(request));
			widgets.add(" ");
			widgets.add(pokerGuiLinkFactory.apiHelp(request));

			return widgets;
		} catch (final PokerServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		} catch (final AuthenticationServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	private String toNumberString(final Long number) {
		if (number == null) {
			return "0";
		} else {
			return String.valueOf(number);
		}
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request) throws ServletException, IOException,
		PermissionDeniedException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			pokerService.expectPokerPlayerOrAdminPermission(sessionIdentifier);
		} catch (final AuthenticationServiceException e) {
			throw new PermissionDeniedException(e);
		} catch (PokerServiceException e) {
			throw new PermissionDeniedException(e);
		}
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}
}
