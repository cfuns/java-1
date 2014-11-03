package de.benjaminborbe.poker.table.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Leaderboard implements Serializable, IsSerializable {

    private String pokerServiceException;

    private List<Player> players = new LinkedList<Player>();

    public Leaderboard() {
    }

    public Leaderboard(final String pokerServiceException) {
        this.pokerServiceException = pokerServiceException;
    }

    public String getPokerServiceException() {
        return pokerServiceException;
    }

    public Collection<Player> getPlayers() {
        return players;
    }

    public void setPlayers(final List<Player> players) {
        this.players = players;
    }

    public Collection<Player> getSortPlayersByScore() {
        Collections.sort(players, new PlayerComperator());
        return players;
    }

    private String makePlayerToString(final Player player) {
        String s = player.getUsername() + " | " + player.getScore();
        return s;
    }

}
