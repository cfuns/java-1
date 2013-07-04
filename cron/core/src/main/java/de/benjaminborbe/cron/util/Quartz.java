package de.benjaminborbe.cron.util;

import de.benjaminborbe.cron.api.CronJob;
import org.quartz.SchedulerException;

public interface Quartz {

	boolean isRunning() throws SchedulerException;

	void start() throws SchedulerException;

	void stop() throws SchedulerException;

	void addCronJob(final CronJob service);

	void removeCronJob(final CronJob service);
}
