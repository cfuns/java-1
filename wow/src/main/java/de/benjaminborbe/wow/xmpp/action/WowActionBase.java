package de.benjaminborbe.wow.xmpp.action;

import org.slf4j.Logger;

import de.benjaminborbe.tools.action.Action;
import de.benjaminborbe.tools.util.ThreadResult;

public abstract class WowActionBase implements Action {

	protected String name;

	private final ThreadResult<Boolean> running;

	private final Logger logger;

	public WowActionBase(final Logger logger, final String name, final ThreadResult<Boolean> running) {
		this.logger = logger;
		this.name = name;
		this.running = running;
	}

	@Override
	public long getWaitTimeout() {
		return 5000;
	}

	@Override
	public long getRetryDelay() {
		return 500;
	}

	@Override
	public void onSuccess() {
		logger.debug(name + " - onSuccess");
	}

	@Override
	public void onFailure() {
		logger.debug(name + " - onFailure");
	}

	@Override
	public boolean validateExecuteResult() {
		logger.trace(name + " - validateExecuteResult");
		return running.get();
	}
}
