package de.benjaminborbe.poker.table.server.servlet;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import de.benjaminborbe.poker.table.client.model.Card;
import de.benjaminborbe.poker.table.client.model.Game;
import de.benjaminborbe.poker.table.client.model.Player;
import de.benjaminborbe.poker.table.client.service.StatusService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class PokerTableStatusServlet extends RemoteServiceServlet implements StatusService {

	private final Logger logger;

	private ArrayList<String> jsonList = new ArrayList<String>();

	@Inject
	public PokerTableStatusServlet(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void log(final String message, final Throwable t) {
		logger.debug(message, t);
	}

	@Override
	public void log(final String msg) {
		logger.debug(msg);
	}

	@Override
	public String processCall(final String payload) throws SerializationException {
		logger.debug("processCall request: " + payload);
		try {
			String result = super.processCall(payload);
			logger.debug("processCall result: " + result);
			return result;
		} catch (RuntimeException e) {
			logger.debug(e.getClass().getName(), e);
			throw e;
		} catch (SerializationException e) {
			logger.debug(e.getClass().getName(), e);
			throw e;
		}
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.debug("service start");
		// Cache the current thread
		final Thread currentThread = Thread.currentThread();
		// We are going to swap the class loader
		final ClassLoader oldContextClassLoader = currentThread.getContextClassLoader();
		try {
			currentThread.setContextClassLoader(this.getClass().getClassLoader());
			super.service(req, resp);
		} catch (Exception e) {
			logger.debug(e.getClass().getName(), e);
			throw new ServletException(e);
		} finally {
			currentThread.setContextClassLoader(oldContextClassLoader);
		}
		logger.debug("service end");
	}

	@Override
	public ArrayList<Game> getGames() {
		logger.debug("getGames");
		final ArrayList<Game> games = new ArrayList<Game>();

		String gameID = getThreadLocalRequest().getParameter("getParameter(\"game_id\")");
		try {
			URL url = new URL("http://bb/bb/poker/game/status/json?token=P2huWY8zZWDd&game_id=6c61792c-a665-4a4f-a64a-298d2c0806aa");
			URLConnection
				connection = url.openConnection();
			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			try {
				JSONObject json = new JSONObject(builder.toString());
				games.add(mapJasonToObject(json));
				logger.debug("map games succcessful");
			} catch (JSONException e) {
				logger.debug("map json to object failed", e);
			}

		} catch (MalformedURLException e) {
			logger.debug("connect to poker status servlet failed", e);
		} catch (IOException e) {
			logger.debug("connect to poker status servlet failed", e);
		} finally {
		}
		logger.debug("found " + games.size() + " games");
		return games;
	}

	private Game mapJasonToObject(JSONObject jsonObject) throws JSONException {
		Game game = new Game();
		game.setGameRound(jsonObject.getString("gameRound"));
		game.setGameSmallBlind(jsonObject.getString("gameSmallBlind"));
		game.setGamePot(jsonObject.getString("gamePot"));
		game.setGameBigBlind(jsonObject.getString("gameBigBlind"));

		game.setGameId(jsonObject.getString("gameId"));
		game.setGameRunning(jsonObject.getBoolean("gameRunning"));
		game.setGameBid(jsonObject.getString("gameBid"));
		game.setGameMaxBid(jsonObject.getString("gameMaxBid"));
		game.setGameName(jsonObject.getString("gameName"));

		String activePlayer = jsonObject.getString("gameActivePlayer");

		JSONArray jsonArray = jsonObject.getJSONArray("gameBoardCards");
		for (int i = 0; i < jsonArray.length(); i++) {
			Card card = new Card();
			card.setCard(jsonArray.get(i).toString());
			game.addCard(card);
		}

		jsonArray = jsonObject.getJSONArray("players");
		for (int i = 0; i < jsonArray.length(); i++) {
			Player player = new Player();
			JSONObject jo = jsonArray.getJSONObject(i);
			player.setUsername(jo.getString("playerName"));
			player.setId(jo.getString("playerId"));
			player.setCredits(jo.getInt("playerCredits"));
			if (activePlayer.equals(player.getId())) {
				game.setActivePlayer(player);
				player.setActivePlayer(true);
			}
			JSONArray jsa = jo.getJSONArray("playerCards");
			for (int j = 0; j < jsa.length(); j++) {
				Card card = new Card();
				card.setCard(jsa.get(j).toString());
				player.addCard(card);
			}
			game.addPlayer(player);
		}
		return game;
	}

	private String getMockString() {
		String a = "{\"gameRound\":2,\"gameSmallBlind\":50,\"gamePot\":300,\"gameBoardCards\":[\"HEARTS_THREE\",\"SPADES_JACK\",\"DIAMONDS_THREE\",\"CLUBS_EIGHT\",\"HEARTS_EIGHT\"],\"gameActivePlayer\":\"fd0e8ed0-7bd9-45cc-b72d-18f1ef51d199\",\"gameBigBlind\":100,\"players\":[{\"playerCards\":[\"HEARTS_FIVE\",\"SPADES_FOUR\"],\"playerName\":\"playerA\",\"playerId\":\"fd0e8ed0-7bd9-45cc-b72d-18f1ef51d199\",\"playerCredits\":998250},{\"playerCards\":[\"HEARTS_KING\",\"SPADES_KING\"],\"playerName\":\"myplayer\",\"playerId\":\"8800b805-3186-4b39-a934-710d2d807ef0\",\"playerCredits\":13900},{\"playerCards\":[\"DIAMONDS_KING\",\"DIAMONDS_FOUR\"],\"playerName\":\"bborbe\",\"playerId\":\"63f8ed37-2bbd-4463-9566-44277df96e54\",\"playerCredits\":997700}],\"gameId\":\"b3fc8e37-0cde-46cd-98af-f6fd4aed03cb\",\"gameRunning\":true,\"gameBid\":100,\"gameMaxBid\":null,\"gameName\":\"testGame\"}";
		return a;
	}
}