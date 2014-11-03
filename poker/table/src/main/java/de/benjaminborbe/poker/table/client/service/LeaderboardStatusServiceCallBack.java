package de.benjaminborbe.poker.table.client.service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import de.benjaminborbe.poker.table.client.model.Leaderboard;
import de.benjaminborbe.poker.table.client.ui.LeaderboardDataSource;

public class LeaderboardStatusServiceCallBack implements AsyncCallback<Leaderboard> {

    private LeaderboardDataSource leaderboardDataSource;

    public LeaderboardStatusServiceCallBack(LeaderboardDataSource leaderboardDataSource) {
        this.leaderboardDataSource = leaderboardDataSource;
    }

    public LeaderboardDataSource getDataSource() {
        return leaderboardDataSource;
    }

    @Override
		public void onFailure(Throwable caught) {
        GWT.log(caught.getMessage());
    }

    @Override
		public void onSuccess(Leaderboard result) {
        leaderboardDataSource.setLeaderboard(result);
    }
}
