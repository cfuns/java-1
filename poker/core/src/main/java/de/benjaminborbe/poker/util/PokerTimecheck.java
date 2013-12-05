package de.benjaminborbe.poker.util;

import de.benjaminborbe.tools.date.CurrentTime;

import javax.inject.Inject;
import java.util.Calendar;

public class PokerTimecheck {

	private final CurrentTime currentTime;

	@Inject
	public PokerTimecheck(final CurrentTime currentTime) {
		this.currentTime = currentTime;
	}

	public boolean timeoutReached(final Calendar lastActionTime, final long autoCallTimeout) {
		return (currentTime.currentTimeMillis() - lastActionTime.getTimeInMillis()) > autoCallTimeout;
	}
}
