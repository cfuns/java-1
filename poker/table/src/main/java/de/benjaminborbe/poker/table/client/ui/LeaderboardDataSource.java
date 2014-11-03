package de.benjaminborbe.poker.table.client.ui;

import de.benjaminborbe.poker.table.client.model.Leaderboard;

import java.io.Serializable;

public class LeaderboardDataSource implements Serializable {

    private static final long serialVersionUID = 6438996743589952343L;

    private Leaderboard leaderboard = null;

    public LeaderboardDataSource() {
    }

    public Leaderboard getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(final Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
    }
}
