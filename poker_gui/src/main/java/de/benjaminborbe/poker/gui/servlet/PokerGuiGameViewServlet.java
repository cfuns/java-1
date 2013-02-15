package de.benjaminborbe.poker.gui.servlet;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerGame;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerPlayer;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.poker.gui.PokerGuiConstants;
import de.benjaminborbe.poker.gui.util.PokerGuiLinkFactory;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import de.benjaminborbe.website.widget.BrWidget;

@Singleton
public class PokerGuiGameViewServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Poker - Game - View";

	private final PokerService pokerService;

	private final PokerGuiLinkFactory pokerGuiLinkFactory;

	@Inject
	public PokerGuiGameViewServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final AuthorizationService authorizationService,
			final CacheService cacheService,
			final PokerService pokerService,
			final PokerGuiLinkFactory pokerGuiLinkFactory) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.pokerService = pokerService;
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
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final PokerGameIdentifier gameIdentifier = pokerService.createGameIdentifier(request.getParameter(PokerGuiConstants.PARAMETER_GAME_ID));
			final PokerGame game = pokerService.getGame(gameIdentifier);

			widgets.add(new H2Widget("Status"));
			if (Boolean.TRUE.equals(game.getRunning())) {
				widgets.add("running = true");
				widgets.add(new BrWidget());
				widgets.add(pokerGuiLinkFactory.gameStop(request, gameIdentifier));
				widgets.add(new BrWidget());
				widgets.add("SmallBlind: " + game.getSmallBlind());
				widgets.add(new BrWidget());
				widgets.add("BigBlind: " + game.getBigBlind());
				widgets.add(new BrWidget());
				widgets.add("Pot: " + game.getPot());
				widgets.add(new BrWidget());
			}
			else {
				widgets.add("running = false");
				widgets.add(new BrWidget());
				widgets.add(pokerGuiLinkFactory.gameStart(request, gameIdentifier));
				widgets.add(new BrWidget());
			}
			{
				final PokerPlayerIdentifier pokerPlayerIdentifier = pokerService.getActivePlayer(gameIdentifier);
				if (pokerPlayerIdentifier != null) {
					final PokerPlayer pokerPlayer = pokerService.getPlayer(pokerPlayerIdentifier);
					widgets.add("active player = ");
					widgets.add(pokerGuiLinkFactory.playerView(request, pokerPlayerIdentifier, pokerPlayer.getName()));
				}
				else {
					widgets.add("active player = none");
				}
				widgets.add(new BrWidget());
			}
			{
				widgets.add(new H2Widget("Board-Cards"));
				final Collection<PokerCardIdentifier> cards = pokerService.getBoardCards(gameIdentifier);
				if (cards.isEmpty()) {
					widgets.add("no cards");
				}
				else {
					final UlWidget ul = new UlWidget();
					for (final PokerCardIdentifier card : cards) {
						ul.add(card.getId());
					}
					widgets.add(ul);
				}
			}
			{
				widgets.add(new H2Widget("Players"));
				final UlWidget ul = new UlWidget();
				for (final PokerPlayerIdentifier pokerPlayerIdentifier : pokerService.getPlayers(gameIdentifier)) {
					final ListWidget list = new ListWidget();
					final PokerPlayer pokerPlayer = pokerService.getPlayer(pokerPlayerIdentifier);
					list.add(pokerGuiLinkFactory.playerView(request, pokerPlayerIdentifier, pokerPlayer.getName()));
					final Collection<PokerCardIdentifier> cards = pokerService.getHandCards(pokerPlayerIdentifier);
					if (cards.isEmpty()) {
						widgets.add("no cards");
					}
					else {
						final UlWidget cardUl = new UlWidget();
						for (final PokerCardIdentifier card : cards) {
							cardUl.add(card.getId());
						}
						list.add(cardUl);
						ul.add(list);
					}
				}
				widgets.add(ul);
			}

			return widgets;
		}
		catch (final PokerServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}
}
