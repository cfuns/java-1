package de.benjaminborbe.poker.gui.servlet.json;

import de.benjaminborbe.poker.api.PokerGame;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.tools.json.JSONObject;

import javax.inject.Inject;

public class PokerGuiJsonHelper {

	private final PokerService pokerService;

	@Inject
	public PokerGuiJsonHelper(final PokerService pokerService) {
		this.pokerService = pokerService;
	}

	public void addGameInfos(final JSONObject jsonObject, final PokerGame game) throws PokerServiceException {
		jsonObject.put("gameId", game.getId());
		jsonObject.put("gameBid", game.getBet());
		jsonObject.put("gameBigBlind", game.getBigBlind());
		jsonObject.put("gameName", game.getName());
		jsonObject.put("gamePot", game.getPot());
		jsonObject.put("gameMaxBid", game.getMaxBid());
		jsonObject.put("gameMinRaiseFactor", game.getMinRaiseFactor());
		jsonObject.put("gameMaxRaiseFactor", game.getMaxRaiseFactor());
		jsonObject.put("gameCreditsNegativeAllowed", game.getCreditsNegativeAllowed());
		jsonObject.put("gameRound", game.getRound());
		jsonObject.put("gameRunning", game.getRunning());
		jsonObject.put("gameSmallBlind", game.getSmallBlind());
		jsonObject.put("gameActivePlayer", pokerService.getActivePlayer(game.getId()));
	}
}
