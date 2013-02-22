package de.benjaminborbe.cron.util;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.HashMap;
import java.util.Map;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.CronConstants;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.cron.job.CronJobOsgi;

@Singleton
public class QuartzImpl implements Quartz {

	private final Logger logger;

	private final GuiceJobFactory guiceJobFactory;

	private final SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

	private final Map<Class<? extends CronJob>, JobKey> jobDetails = new HashMap<Class<? extends CronJob>, JobKey>();

	private Scheduler sched = null;

	@Inject
	public QuartzImpl(final Logger logger, final GuiceJobFactory guiceJobFactory) {
		this.logger = logger;
		this.guiceJobFactory = guiceJobFactory;
	}

	protected synchronized Scheduler getScheduler() throws SchedulerException {
		logger.trace("QuartzImpl.getScheduler()");
		if (sched == null) {
			sched = schedFact.getScheduler();
			sched.setJobFactory(guiceJobFactory);
		}
		return sched;
	}

	protected synchronized void removeScheduler() throws SchedulerException {
		logger.trace("QuartzImpl.removeScheduler()");
		sched = null;
	}

	@Override
	public synchronized void start() throws SchedulerException {
		logger.trace("QuartzImpl.start()");
		final Scheduler scheduler = getScheduler();
		scheduler.start();
	}

	/**
	 * used directly to deactivate ALL cronjobs in Activator.stop
	 * 
	 * @throws SchedulerException
	 */
	@Override
	public synchronized void stop() throws SchedulerException {
		logger.trace("QuartzImpl.stop()");
		final Scheduler scheduler = getScheduler();
		scheduler.shutdown();
		// scheduler.clear();
		removeScheduler();
	}

	@Override
	public void addCronJob(final CronJob cronJob) {
		logger.trace("QuartzImpl.addCronJob()");
		if (jobDetails.containsKey(cronJob.getClass())) {
			logger.trace("skip add cronJob, already added");
		}
		else {
			try {
				final String name = cronJob.getClass().getName();
				final JobDetail jobDetail = newJob(CronJobOsgi.class).withIdentity(name, name).usingJobData(CronConstants.JOB_NAME, name).build();
				final CronTrigger trigger = newTrigger().withIdentity(name, name).withSchedule(cronSchedule(cronJob.getScheduleExpression())).forJob(name, name).build();

				jobDetails.put(cronJob.getClass(), jobDetail.getKey());

				// add job to scheduler
				getScheduler().scheduleJob(jobDetail, trigger);
				logger.trace("job scheduled");
			}
			catch (final SchedulerException e) {
				logger.error("SchedulerException", e);
			}
		}
	}

	@Override
	public void removeCronJob(final CronJob cronJob) {
		logger.trace("QuartzImpl.removeCronJob()");
		if (jobDetails.containsKey(cronJob.getClass())) {
			try {
				getScheduler().deleteJob(jobDetails.get(cronJob.getClass()));
				jobDetails.remove(cronJob.getClass());
				logger.trace("job deleted");
			}
			catch (final SchedulerException e) {
				logger.error("SchedulerException", e);
			}
		}
		else {
			logger.trace("skip remove cronJob, not added");
		}
	}

	@Override
	public boolean isRunning() throws SchedulerException {
		return getScheduler().isStarted();
	}

}
