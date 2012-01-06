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
		logger.debug("QuartzImpl.getScheduler()");
		if (sched == null) {
			sched = schedFact.getScheduler();
			sched.setJobFactory(guiceJobFactory);
		}
		return sched;
	}

	protected synchronized void removeScheduler() throws SchedulerException {
		logger.debug("QuartzImpl.removeScheduler()");
		sched = null;
	}

	@Override
	public synchronized void start() throws SchedulerException {
		logger.debug("QuartzImpl.start()");
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
		logger.debug("QuartzImpl.stop()");
		final Scheduler scheduler = getScheduler();
		scheduler.shutdown();
		removeScheduler();
	}

	@Override
	public void addCronJob(final CronJob cronJob) {
		logger.debug("QuartzImpl.addCronJob()");
		if (jobDetails.containsKey(cronJob.getClass())) {
			logger.debug("skip add cronJob, allready added");
		}
		else {
			try {
				final String name = cronJob.getClass().getName();
				final JobDetail jobDetail = newJob(CronJobOsgi.class).withIdentity(name, name).usingJobData("name", name).build();
				final CronTrigger trigger = newTrigger().withIdentity(name, name).withSchedule(cronSchedule(cronJob.getScheduleExpression())).forJob(name, name).build();

				jobDetails.put(cronJob.getClass(), jobDetail.getKey());

				// add job to scheduler
				getScheduler().scheduleJob(jobDetail, trigger);
				logger.debug("job scheduled");
			}
			catch (final SchedulerException e) {
				logger.error("SchedulerException", e);
			}
		}
	}

	@Override
	public void removeCronJob(final CronJob cronJob) {
		logger.debug("QuartzImpl.removeCronJob()");
		if (jobDetails.containsKey(cronJob.getClass())) {
			try {
				getScheduler().deleteJob(jobDetails.get(cronJob.getClass()));
				jobDetails.remove(cronJob.getClass());
				logger.debug("job deleted");
			}
			catch (final SchedulerException e) {
				logger.error("SchedulerException", e);
			}
		}
		else {
			logger.debug("skip remove cronJob, not added");
		}
	}

}
