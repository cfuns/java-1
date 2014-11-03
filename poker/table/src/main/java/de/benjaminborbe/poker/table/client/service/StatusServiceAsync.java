package de.benjaminborbe.poker.table.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import de.benjaminborbe.poker.table.client.model.Game;
import de.benjaminborbe.poker.table.client.model.Leaderboard;

public interface StatusServiceAsync {

	void getGame(final String gameId, final AsyncCallback<Game> async);

	void getAllPlayers(final AsyncCallback<Leaderboard> async);
}
