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
public class PokerGuiPlayerStatusJsonServlet extends PokerGuiPlayerJsonServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private final PokerService pokerService;

	@Inject
	public PokerGuiPlayerStatusJsonServlet(
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
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider, pokerService, pokerGuiConfig);
		this.pokerService = pokerService;
	}

	@Override
	protected void doAction(final HttpServletRequest request, final HttpServletResponse response) throws PokerServiceException, ValidationException,
		ServletException, IOException, PermissionDeniedException, LoginRequiredException {
		final JSONObject jsonObject = new JSONObjectSimple();
		final PokerPlayerIdentifier playerIdentifier = pokerService.createPlayerIdentifier(request.getParameter(PokerGuiConstants.PARAMETER_PLAYER_ID));
		final PokerPlayer currentPlayer = pokerService.getPlayer(playerIdentifier);
		jsonObject.put("gameId", currentPlayer.getGame());
		if (currentPlayer.getGame() != null) {
			final PokerGame game = pokerService.getGame(currentPlayer.getGame());
			jsonObject.put("gameBid", game.getBet());
			jsonObject.put("gameBigBlind", game.getBigBlind());
			jsonObject.put("gameName", game.getName());
			jsonObject.put("gamePot", game.getPot());
			jsonObject.put("gameMaxBid", game.getMaxBid());
			jsonObject.put("gameRound", game.getRound());
			jsonObject.put("gameRunning", game.getRunning());
			jsonObject.put("gameSmallBlind", game.getSmallBlind());
			jsonObject.put("gameActivePlayer", pokerService.getActivePlayer(currentPlayer.getGame()));
			final JSONArray jsonPlayers = new JSONArraySimple();
			for (final PokerPlayerIdentifier pid : game.getPlayers()) {
				final JSONObject jsonPlayerObject = new JSONObjectSimple();
				PokerPlayer otherPlayer = pokerService.getPlayer(pid);
				jsonPlayerObject.put("playerId", otherPlayer.getId());
				jsonPlayerObject.put("playerName", otherPlayer.getName());
				jsonPlayerObject.put("playerCredits", otherPlayer.getAmount());
				jsonPlayerObject.put("playerBet", otherPlayer.getBet());
				jsonPlayers.add(jsonPlayerObject);
			}
			jsonObject.put("players", jsonPlayers);

			final JSONArray jsonBoardCards = new JSONArraySimple();
			for (PokerCardIdentifier cid : game.getBoardCards()) {
				jsonBoardCards.add(cid);
			}
			jsonObject.put("boardCards", jsonBoardCards);
		}
		final JSONArray jsonPlayerCards = new JSONArraySimple();
		for (final PokerCardIdentifier cid : currentPlayer.getCards()) {
			jsonPlayerCards.add(cid);
		}
		jsonObject.put("playerCards", jsonPlayerCards);
		jsonObject.put("playerId", currentPlayer.getId());
		jsonObject.put("playerName", currentPlayer.getName());
		jsonObject.put("playerCredits", currentPlayer.getAmount());
		printJson(response, jsonObject);
	}
}
