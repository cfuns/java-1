package de.benjaminborbe.analytics.util;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;

public class AnalyticsAggregator {

	private final class AggregateRunnable implements Runnable {

		@Override
		public void run() {
		}
	}

	private final RunOnlyOnceATime runOnlyOnceATime;

	private final Logger logger;

	@Inject
	public AnalyticsAggregator(final Logger logger, final RunOnlyOnceATime runOnlyOnceATime) {
		this.logger = logger;
		this.runOnlyOnceATime = runOnlyOnceATime;
	}

	public boolean aggregate() {
		logger.debug("analytics aggregate - started");
		if (runOnlyOnceATime.run(new AggregateRunnable())) {
			logger.debug("analytics aggregate - finished");
			return true;
		}
		else {
			logger.debug("analytics aggregate - skipped");
			return false;
		}
	}

}
