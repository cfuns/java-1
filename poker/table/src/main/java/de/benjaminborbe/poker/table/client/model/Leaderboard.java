package de.benjaminborbe.poker.table.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.ArrayList;

public class Leaderboard implements Serializable, IsSerializable {

    private String pokerServiceException;

    private ArrayList<Player> players = new ArrayList<Player>();

    public Leaderboard() {
    }

    public Leaderboard(final String pokerServiceException) {
        this.pokerServiceException = pokerServiceException;
    }

    public String getPokerServiceException() {
        return pokerServiceException;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(final ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<String> getPlayersList() {
        final ArrayList<String> list = new ArrayList<String>();

        for (final Player player : players) {
            String s = player.getUsername() + ", " + player.getScore();
            list.add(s);
        }
        return list;
    }

}
