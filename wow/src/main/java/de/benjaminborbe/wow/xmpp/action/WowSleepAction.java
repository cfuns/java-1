package de.benjaminborbe.wow.xmpp.action;

import org.slf4j.Logger;

import de.benjaminborbe.tools.util.ThreadResult;

public class WowSleepAction extends WowActionBase {

	private final long sleep;

	private final Logger logger;

	public WowSleepAction(final Logger logger, final String name, final ThreadResult<Boolean> running, final long sleep) {
		super(logger, name, running);
		this.logger = logger;
		this.sleep = sleep;
	}

	@Override
	public void execute() {
		logger.debug(name + " - execute started");
		try {
			Thread.sleep(sleep);
		}
		catch (final InterruptedException e) {
		}
		logger.debug(name + " - execute finished");
	}

}
