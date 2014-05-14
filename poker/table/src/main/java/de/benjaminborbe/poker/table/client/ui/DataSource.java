package de.benjaminborbe.poker.table.client.ui;

import de.benjaminborbe.poker.table.client.model.Game;

import java.io.Serializable;

public class DataSource implements Serializable {

	private static final long serialVersionUID = 6438969743589952343L;

	private Game game = null;

	public DataSource() {
	}

	public Game getGame() {
		return game;
	}

	public void setGame(final Game game) {
		this.game = game;
	}

}
