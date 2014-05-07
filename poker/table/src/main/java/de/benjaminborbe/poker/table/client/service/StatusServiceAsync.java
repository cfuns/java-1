package de.benjaminborbe.poker.table.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import de.benjaminborbe.poker.table.client.model.Game;

import java.util.ArrayList;

public interface StatusServiceAsync {

	void getGame(String id, final AsyncCallback<Game> async);

	void getGames(final AsyncCallback<ArrayList<Game>> async);
}
