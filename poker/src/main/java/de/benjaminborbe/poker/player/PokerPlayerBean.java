package de.benjaminborbe.poker.player;

import java.util.Calendar;
import de.benjaminborbe.poker.api.PokerPlayer;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class PokerPlayerBean extends EntityBase<PokerPlayerIdentifier> implements PokerPlayer, HasCreated, HasModified {

	private static final long serialVersionUID = -1631188424667532085L;

	private PokerPlayerIdentifier id;

	private String name;

	private Calendar created;

	private Calendar modified;

	private Long amount;

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
	public PokerPlayerIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final PokerPlayerIdentifier id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public Long getAmount() {
		return amount;
	}

}
