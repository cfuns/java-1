package de.benjaminborbe.cron.util;

import org.quartz.SchedulerException;
import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.tools.util.ThreadRunner;

public class CronManager {

	private final class QuartzStartRunnable implements Runnable {

		@Override
		public void run() {
			// cron stoppen
			try {
				quartz.start();
			}
			catch (final SchedulerException e) {
				logger.error("SchedulerException", e);
			}
		}
	}

	private static final long DELAY = 5 * 60 * 1000;

	private final Quartz quartz;

	private final Logger logger;

	private final ThreadRunner threadRunner;

	@Inject
	public CronManager(final Logger logger, final ThreadRunner threadRunner, final Quartz quartz) {
		this.logger = logger;
		this.threadRunner = threadRunner;
		this.quartz = quartz;
	}

	public void start() {
		logger.debug("start cron in " + DELAY + " ms");
		threadRunner.runIn(DELAY, new QuartzStartRunnable());
	}

	public void stop() {
		try {
			logger.debug("stop cron");
			quartz.stop();
		}
		catch (final SchedulerException e) {
			logger.trace("SchedulerException", e);
		}
	}
}
