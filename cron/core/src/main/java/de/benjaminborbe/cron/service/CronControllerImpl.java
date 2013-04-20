package de.benjaminborbe.cron.service;

import org.quartz.SchedulerException;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.benjaminborbe.cron.api.CronController;
import de.benjaminborbe.cron.api.CronControllerException;
import de.benjaminborbe.cron.util.Quartz;

@Singleton
public class CronControllerImpl implements CronController {

	private final Logger logger;

	private final Quartz quartz;

	@Inject
	public CronControllerImpl(final Logger logger, final Quartz quartz) {
		this.logger = logger;
		this.quartz = quartz;
	}

	@Override
	public void start() throws CronControllerException {
		try {
			logger.trace("start");
			quartz.start();
		}
		catch (final SchedulerException e) {
			throw new CronControllerException("SchedulerException", e);
		}
	}

	@Override
	public void stop() throws CronControllerException {
		try {
			logger.trace("stop");
			quartz.stop();
		}
		catch (final SchedulerException e) {
			throw new CronControllerException("SchedulerException", e);
		}
	}

	@Override
	public boolean isRunning() throws CronControllerException {
		try {
			logger.trace("isRunning");
			return quartz.isRunning();
		}
		catch (final SchedulerException e) {
			throw new CronControllerException("SchedulerException", e);
		}
	}

}
