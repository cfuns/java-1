package de.benjaminborbe.tools.synchronize;

import org.slf4j.Logger;

import javax.inject.Inject;

public class RunOnlyOnceATime {

	private final Logger logger;

	private Boolean running = Boolean.FALSE;

	@Inject
	public RunOnlyOnceATime(final Logger logger) {
		this.logger = logger;
	}

	public boolean run(final Runnable runnable) {
		logger.trace("started");
		if (isNotRunning()) {
			try {
				runnable.run();
			}
			finally {
				finished();
			}
			logger.trace("finished");
			return true;
		}
		else {
			logger.trace("skipped");
			return false;
		}
	}

	private void finished() {
		synchronized (running) {
			running = Boolean.FALSE;
		}
	}

	private boolean isNotRunning() {
		synchronized (running) {
			if (running) {
				return false;
			}
			running = Boolean.TRUE;
		}
		return true;
	}
}
