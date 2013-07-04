package de.benjaminborbe.poker.gui.servlet.json;

import com.google.inject.Provider;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerGame;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerPlayer;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.poker.gui.PokerGuiConstants;
import de.benjaminborbe.poker.gui.config.PokerGuiConfig;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.json.JSONArray;
import de.benjaminborbe.tools.json.JSONArraySimple;
import de.benjaminborbe.tools.json.JSONObject;
import de.benjaminborbe.tools.json.JSONObjectSimple;
import de.benjaminborbe.tools.url.UrlUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class PokerGuiGameStatusJsonServlet extends PokerGuiJsonServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private final PokerService pokerService;

	private final PokerGuiConfig pokerGuiConfig;

	@Inject
	public PokerGuiGameStatusJsonServlet(
		final Logger logger,
		final UrlUtil urlUtil,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final Provider<HttpContext> httpContextProvider,
		final PokerService pokerService,
		final PokerGuiConfig pokerGuiConfig
	) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider, pokerGuiConfig);
		this.pokerService = pokerService;
		this.pokerGuiConfig = pokerGuiConfig;
	}

	@Override
	protected void doAction(final HttpServletRequest request, final HttpServletResponse response) throws PokerServiceException, ValidationException,
		ServletException, IOException, PermissionDeniedException, LoginRequiredException {
		final JSONObject jsonObject = new JSONObjectSimple();
		final PokerGameIdentifier gameIdentifier = pokerService.createGameIdentifier(request.getParameter(PokerGuiConstants.PARAMETER_GAME_ID));
		final PokerGame game = pokerService.getGame(gameIdentifier);
		jsonObject.put("gameId", game.getId());
		jsonObject.put("gameBid", game.getBet());
		jsonObject.put("gameBigBlind", game.getBigBlind());
		jsonObject.put("gameName", game.getName());
		jsonObject.put("gamePot", game.getPot());
		jsonObject.put("gameMaxBid", game.getMaxBid());
		jsonObject.put("gameRound", game.getRound());
		jsonObject.put("gameRunning", game.getRunning());
		jsonObject.put("gameSmallBlind", game.getSmallBlind());
		jsonObject.put("gameActivePlayer", pokerService.getActivePlayer(game.getId()));

		// add board cards
		final JSONArray jsonBoardCards = new JSONArraySimple();
		for (PokerCardIdentifier card : game.getBoardCards()) {
			jsonBoardCards.add(card);
		}
		jsonObject.put("gameBoardCards", jsonBoardCards);

		// add players
		final JSONArray jsonPlayers = new JSONArraySimple();
		for (final PokerPlayerIdentifier pid : game.getPlayers()) {
			PokerPlayer player = pokerService.getPlayer(pid);
			JSONObject jsonPlayer = new JSONObjectSimple();
			jsonPlayer.put("playerId", player.getId());
			jsonPlayer.put("playerName", player.getName());
			jsonPlayer.put("playerCredits", player.getAmount());
			jsonPlayer.put("playerBet", player.getBet());
			final JSONArray jsonPlayerCards = new JSONArraySimple();
			for (PokerCardIdentifier cid : player.getCards()) {
				jsonPlayerCards.add(cid);
			}
			jsonPlayer.put("playerCards", jsonPlayerCards);
			jsonPlayers.add(jsonPlayer);
		}
		jsonObject.put("players", jsonPlayers);

		printJson(response, jsonObject);
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request) throws ServletException, IOException, PermissionDeniedException, LoginRequiredException {
		if (!pokerGuiConfig.getJsonApiDashboardToken().equals(request.getParameter(PokerGuiConstants.PARAMETER_TOKEN))) {
			throw new PermissionDeniedException("invalid token");
		}
	}
}
