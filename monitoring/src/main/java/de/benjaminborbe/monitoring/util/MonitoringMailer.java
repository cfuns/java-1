package de.benjaminborbe.monitoring.util;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;

public class MonitoringMailer {

	private final RunOnlyOnceATime runOnlyOnceATime;

	private final Logger logger;

	private final class Action implements Runnable {

		@Override
		public void run() {
		}
	}

	@Inject
	public MonitoringMailer(final Logger logger, final RunOnlyOnceATime runOnlyOnceATime) {
		this.logger = logger;
		this.runOnlyOnceATime = runOnlyOnceATime;
	}

	public boolean mail() {
		logger.debug("monitoring mailer - started");
		if (runOnlyOnceATime.run(new Action())) {
			logger.debug("monitoring mailer - finished");
			return true;
		}
		else {
			logger.debug("monitoring mailer - skipped");
			return false;
		}
	}
}
