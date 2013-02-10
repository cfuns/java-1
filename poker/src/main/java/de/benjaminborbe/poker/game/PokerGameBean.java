package de.benjaminborbe.poker.game;

import java.util.Calendar;
import de.benjaminborbe.poker.api.PokerGame;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class PokerGameBean extends EntityBase<PokerGameIdentifier> implements PokerGame, HasCreated, HasModified {

	private static final long serialVersionUID = -1631188424667532085L;

	private PokerGameIdentifier id;

	private String name;

	private Calendar created;

	private Calendar modified;

	private Boolean running;

	private Long smallBlind;

	private Long bigBlind;

	private PokerPlayerIdentifier activePlayer;

	@Override
	public Calendar getCreated() {
		return created;
	}

	@Override
	public Calendar getModified() {
		return modified;
	}

	@Override
	public void setCreated(final Calendar created) {
		this.created = created;
	}

	@Override
	public void setModified(final Calendar modified) {
		this.modified = modified;
	}

	@Override
	public PokerGameIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final PokerGameIdentifier id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public Boolean getRunning() {
		return running;
	}

	@Override
	public PokerPlayerIdentifier getActivePlayer() {
		return activePlayer;
	}

	@Override
	public Long getBigBlind() {
		return bigBlind;
	}

	@Override
	public Long getSmallBlind() {
		return smallBlind;
	}

	public void setRunning(final Boolean running) {
		this.running = running;
	}

	public void setSmallBlind(final Long smallBlind) {
		this.smallBlind = smallBlind;
	}

	public void setBigBlind(final Long bigBlind) {
		this.bigBlind = bigBlind;
	}

	public void setActivePlayer(final PokerPlayerIdentifier activePlayer) {
		this.activePlayer = activePlayer;
	}

}
