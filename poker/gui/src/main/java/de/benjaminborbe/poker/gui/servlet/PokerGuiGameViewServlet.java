package de.benjaminborbe.poker.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.CssResource;
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
import de.benjaminborbe.poker.gui.util.PokerCardTranslater;
import de.benjaminborbe.poker.gui.util.PokerGuiLinkFactory;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputHiddenWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.CssResourceImpl;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.SingleTagWidget;
import de.benjaminborbe.website.util.StringWidget;
import de.benjaminborbe.website.util.UlWidget;
import de.benjaminborbe.website.widget.BrWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Collection;

@Singleton
public class PokerGuiGameViewServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Poker - Game - View";

	private final PokerService pokerService;

	private final PokerGuiLinkFactory pokerGuiLinkFactory;

	private final PokerCardTranslater pokerCardTranslater;

	private final AuthenticationService authenticationService;

	private final CurrentTime currentTime;

	@Inject
	public PokerGuiGameViewServlet(
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
		final PokerCardTranslater pokerCardTranslater,
		final CurrentTime currentTime
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.pokerService = pokerService;
		this.pokerGuiLinkFactory = pokerGuiLinkFactory;
		this.pokerCardTranslater = pokerCardTranslater;
		this.authenticationService = authenticationService;
		this.currentTime = currentTime;
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

			final PokerGameIdentifier gameIdentifier = pokerService.createGameIdentifier(request.getParameter(PokerGuiConstants.PARAMETER_GAME_ID));
			final PokerGame game = pokerService.getGame(gameIdentifier);

			widgets.add("StartCredits: " + game.getStartCredits());
			widgets.add(new BrWidget());

			widgets.add(new H2Widget("Status"));
			if (Boolean.TRUE.equals(game.getRunning())) {
				widgets.add("running = true");
				widgets.add(new BrWidget());
				if (pokerService.hasPokerAdminPermission(sessionIdentifier)) {
					widgets.add(pokerGuiLinkFactory.gameStop(request, gameIdentifier));
					widgets.add(new BrWidget());
				}
				widgets.add("Round: " + game.getRound());
				widgets.add(new BrWidget());

				widgets.add("SmallBlind: " + game.getSmallBlind());
				widgets.add(new BrWidget());

				widgets.add("BigBlind: " + game.getBigBlind());
				widgets.add(new BrWidget());

				widgets.add("Pot: " + game.getPot());
				widgets.add(new BrWidget());

				widgets.add("Bid: " + game.getBet());
				widgets.add(new BrWidget());

				widgets.add("ButtonPlayer: ");
				widgets.add(playerIdentifierToWidget(request, pokerService.getButtonPlayer(gameIdentifier)));
				widgets.add(new BrWidget());

				widgets.add("SmallBlindPlayer: ");
				widgets.add(playerIdentifierToWidget(request, pokerService.getSmallBlindPlayer(gameIdentifier)));
				widgets.add(new BrWidget());

				widgets.add("BigBlindPlayer: ");
				widgets.add(playerIdentifierToWidget(request, pokerService.getBigBlindPlayer(gameIdentifier)));
				widgets.add(new BrWidget());

				widgets.add("AutoFoldTimeout: ");
				widgets.add(getAutoFoldTimeoutAsString(game));
				widgets.add(new BrWidget());

				widgets.add("Time Remaining: ");
				widgets.add(getTimeRemainingAsString(game));
				widgets.add(new BrWidget());

				widgets.add("Credit History: ");
				widgets.add(pokerGuiLinkFactory.createHistoryLink(request, "history", game.getPlayers()));
				widgets.add(new BrWidget());

				widgets.add("MaxBid: " + game.getMaxBid());
				widgets.add(new BrWidget());

				widgets.add("MinRaiseFactor: " + game.getMinRaiseFactor());
				widgets.add(new BrWidget());

				widgets.add("MaxRaiseFactor: " + game.getMaxRaiseFactor());
				widgets.add(new BrWidget());

				final PokerPlayerIdentifier playerIdentifier = pokerService.getActivePlayer(gameIdentifier);
				if (playerIdentifier != null) {
					widgets.add("ActivePlayer: ");
					widgets.add(playerIdentifierToWidget(request, playerIdentifier));
					widgets.add(new BrWidget());
					if (pokerService.hasPokerAdminPermission(sessionIdentifier)) {
						widgets.add(new H2Widget("Actions"));
						widgets.add(pokerGuiLinkFactory.call(request, gameIdentifier, playerIdentifier));
						widgets.add(new BrWidget());
						widgets.add(pokerGuiLinkFactory.fold(request, gameIdentifier, playerIdentifier));
						widgets.add(new BrWidget());
						final FormWidget form = new FormWidget(request.getContextPath() + "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_ACTION_RAISE);
						form.addFormInputWidget(new FormInputHiddenWidget(PokerGuiConstants.PARAMETER_GAME_ID).addValue(gameIdentifier));
						form.addFormInputWidget(new FormInputHiddenWidget(PokerGuiConstants.PARAMETER_PLAYER_ID).addValue(playerIdentifier));
						form.addFormInputWidget(new FormInputTextWidget(PokerGuiConstants.PARAMETER_AMOUNT).addLabel("Amount:").addDefaultValue(game.getBet()));
						form.addFormInputWidget(new FormInputSubmitWidget("raise"));
						widgets.add(form);
						widgets.add(new BrWidget());
					}
				} else {
					widgets.add("active player = none");
					widgets.add(new BrWidget());
				}
			} else {
				widgets.add("running = false");
				widgets.add(new BrWidget());
				if (pokerService.hasPokerAdminPermission(sessionIdentifier)) {
					widgets.add(pokerGuiLinkFactory.gameStart(request, gameIdentifier));
					widgets.add(new BrWidget());
					widgets.add(pokerGuiLinkFactory.gameJoinAllPlayers(request, gameIdentifier));
					widgets.add(new BrWidget());
					widgets.add(pokerGuiLinkFactory.gameUpdate(request, gameIdentifier));
					widgets.add(new BrWidget());
				}
			}
			{
				widgets.add(new H2Widget("Board / Flop+Turn+River"));
				final Collection<PokerCardIdentifier> cards = pokerService.getBoardCards(gameIdentifier);
				if (cards.isEmpty()) {
					widgets.add("no cards");
				} else {
					final UlWidget ul = new UlWidget();
					for (final PokerCardIdentifier card : cards) {
						ul.add(pokerCardTranslater.translate(card));
					}
					widgets.add(ul);
				}
			}
			{
				widgets.add(new H2Widget("Players"));
				final UlWidget ul = new UlWidget();
				final Collection<PokerPlayerIdentifier> activePlayers = pokerService.getActivePlayers(gameIdentifier);
				final Collection<PokerPlayerIdentifier> players = pokerService.getPlayers(gameIdentifier);
				if (players.isEmpty()) {
					widgets.add("no players");
					widgets.add(new BrWidget());
				} else {
					for (final PokerPlayerIdentifier playerIdentifier : players) {
						final ListWidget list = new ListWidget();
						final PokerPlayer player = pokerService.getPlayer(playerIdentifier);
						list.add(pokerGuiLinkFactory.playerView(request, playerIdentifier, player.getName()));
						list.add(" ");
						list.add(getPlayerStatusAsString(activePlayers, playerIdentifier));
						list.add(" ");
						list.add("Score: " + player.getScore());
						list.add(" ");
						list.add("Credit: " + player.getAmount());
						list.add(" ");
						list.add("InPot: " + player.getBet());
						list.add(new BrWidget());
						final Collection<PokerCardIdentifier> cards = pokerService.getHandCards(playerIdentifier);
						if (cards.isEmpty()) {
							list.add("no cards");
							list.add(new BrWidget());
						} else {
							final UlWidget cardUl = new UlWidget();
							for (final PokerCardIdentifier card : cards) {
								cardUl.add(pokerCardTranslater.translate(card));
							}
							list.add(cardUl);
						}
						ul.add(list);
					}
					widgets.add(ul);
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

	private String getPlayerStatusAsString(final Collection<PokerPlayerIdentifier> activePlayers, final PokerPlayerIdentifier playerIdentifier) {
		return activePlayers.contains(playerIdentifier) ? "active" : "inactive";
	}

	private String getTimeRemainingAsString(final PokerGame game) {
		if (game.getActivePositionTime() != null) {
			return String.valueOf(currentTime.currentTimeMillis() - game.getActivePositionTime().getTimeInMillis());
		} else {
			return "-";
		}
	}

	private String getAutoFoldTimeoutAsString(final PokerGame game) {
		return game.getAutoFoldTimeout() != null ? String.valueOf(game.getAutoFoldTimeout()) : "-";
	}

	public Widget playerIdentifierToWidget(
		final HttpServletRequest request,
		final PokerPlayerIdentifier playerIdentifier
	) throws PokerServiceException, MalformedURLException,
		UnsupportedEncodingException {
		final PokerPlayer player = pokerService.getPlayer(playerIdentifier);
		if (player != null) {
			return pokerGuiLinkFactory.playerView(request, playerIdentifier, player.getName());
		} else {
			return new StringWidget("-");
		}
	}

	@Override
	protected Collection<CssResource> getCssResources(final HttpServletRequest request, final HttpServletResponse response) {
		final Collection<CssResource> result = super.getCssResources(request, response);
		result.add(new CssResourceImpl(request.getContextPath() + "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_CSS_STYLE));
		return result;
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
