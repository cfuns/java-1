package de.benjaminborbe.tools.synchronize;

import org.slf4j.Logger;

import com.google.inject.Inject;

public class RunOnlyOnceATime {

	private final Logger logger;

	private Boolean running = Boolean.FALSE;

	@Inject
	public RunOnlyOnceATime(final Logger logger) {
		this.logger = logger;
	}

	public void run(final Runnable runnable) {
		logger.debug("started");
		if (isNotRunning()) {
			runnable.run();
			finished();
			logger.debug("finished");
		}
		else {
			logger.debug("skipped");
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
