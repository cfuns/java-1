package de.benjaminborbe.poker.table.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import de.benjaminborbe.poker.table.client.model.Game;
import de.benjaminborbe.poker.table.client.model.Leaderboard;

@RemoteServiceRelativePath("statusService")
public interface StatusService extends RemoteService {

	Game getGame(String gameId);

	Leaderboard getAllPlayers();
}