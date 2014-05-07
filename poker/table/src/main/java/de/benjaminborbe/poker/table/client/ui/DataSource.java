package de.benjaminborbe.poker.table.client.ui;

import de.benjaminborbe.poker.table.client.model.Game;

import java.io.Serializable;
import java.util.ArrayList;

public class DataSource implements Serializable {

	ArrayList<Game> games = new ArrayList<Game>();

	public DataSource() {
	}

	public ArrayList<Game> getGames() {
		return games;
	}

	public void setGames(final ArrayList<Game> games) {
		this.games = games;
	}

}
