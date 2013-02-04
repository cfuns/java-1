package de.benjaminborbe.tools.synchronize;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;

public class RunOnlyOnceATimeByType {

	private final Set<String> running = new HashSet<String>();

	private final Logger logger;

	@Inject
	public RunOnlyOnceATimeByType(final Logger logger) {
		this.logger = logger;
	}

	public boolean run(final String type, final Runnable runnable) {
		logger.trace("started");
		if (isNotRunning(type)) {
			try {
				runnable.run();
			}
			finally {
				finished(type);
			}
			logger.trace("finished");
			return true;
		}
		else {
			logger.trace("skipped");
			return false;
		}
	}

	private void finished(final String type) {
		synchronized (running) {
			running.remove(type);
		}
	}

	private boolean isNotRunning(final String type) {
		synchronized (running) {
			if (running.contains(type)) {
				return false;
			}
			running.add(type);
		}
		return true;
	}
}
