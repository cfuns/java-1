package de.benjaminborbe.cron.util;

import org.quartz.SchedulerException;
import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.tools.thread.ThreadUtil;

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

	private final ThreadUtil threadUtil;

	private final Quartz quartz;

	private final Logger logger;

	@Inject
	public CronManager(final Logger logger, final ThreadUtil threadUtil, final Quartz quartz) {
		this.logger = logger;
		this.threadUtil = threadUtil;
		this.quartz = quartz;
	}

	public void start() {
		logger.debug("start cron in " + DELAY + " ms");
		threadUtil.runIn(DELAY, new QuartzStartRunnable());
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
