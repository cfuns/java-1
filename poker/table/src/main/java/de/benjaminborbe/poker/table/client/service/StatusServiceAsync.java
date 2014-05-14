package de.benjaminborbe.poker.table.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import de.benjaminborbe.poker.table.client.model.Game;

public interface StatusServiceAsync {

	void getGame(String gameId, final AsyncCallback<Game> async);
}
