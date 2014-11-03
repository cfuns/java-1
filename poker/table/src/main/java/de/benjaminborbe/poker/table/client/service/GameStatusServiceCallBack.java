package de.benjaminborbe.poker.table.client.service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import de.benjaminborbe.poker.table.client.model.Game;
import de.benjaminborbe.poker.table.client.ui.GameDataSource;

public class GameStatusServiceCallBack implements AsyncCallback<Game> {

    private GameDataSource gameDataSource;

    public GameStatusServiceCallBack(GameDataSource gameDataSource) {
        this.gameDataSource = gameDataSource;
    }

    public GameDataSource getDataSource() {
        return gameDataSource;
    }

	  @Override
    public void onFailure(Throwable caught) {
        GWT.log(caught.getMessage());
    }

	 @Override
    public void onSuccess(Game result) {
        gameDataSource.setGame(result);
    }

}
