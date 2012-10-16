package de.benjaminborbe.wow.xmpp;

import de.benjaminborbe.tools.action.Action;

public abstract class ActionBase implements Action {

	@Override
	public long getWaitTimeout() {
		return 5000;
	}

	@Override
	public long getRetryDelay() {
		return 500;
	}

}
