package de.benjaminborbe.cron.util;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.cron.job.EchoJob;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.JobBuilder.*;

public class Quartz {

	/* @formatter:off  <- PLEASE SEE FORMATTER SETTINGS                   s     m     h     d     m     dw     y */
  private static final String ECHO_JOB                               = "*/10  *     *     *     *     ?";
  // @formatter:on

	private final Logger logger;

	private final GuiceJobFactory guiceJobFactory;

	private final SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

	private static Scheduler sched = null;

	@Inject
	public Quartz(final Logger logger, final GuiceJobFactory guiceJobFactory) {
		this.logger = logger;
		this.guiceJobFactory = guiceJobFactory;
	}

	public synchronized void start() throws SchedulerException {
		// stop existing cron
		if (sched != null) {
			logger.debug("Quartz - shutdown existing cron job");
			sched.shutdown();
		}

		logger.debug("Quartz - start ...");

		sched = schedFact.getScheduler();
		sched.setJobFactory(guiceJobFactory);

		// jobs
		scheduleEcho();

		sched.start();

		logger.debug("Quartz started");
	}

	/**
	 * used directly to deactivate ALL cronjobs in Activator.stop
	 * 
	 * @throws SchedulerException
	 */
	public synchronized void stop() throws SchedulerException {
		if (sched == null) {
			logger.warn("Quartz already stopped!");
			return;
		}
		else {
			logger.debug("Quartz stopping ...");
		}

		sched.shutdown();
		sched = null;

		logger.debug("Quartz stopped");

	}

	public Scheduler getCurrentScheduler() {
		return sched;
	}

	private void scheduleEcho() throws SchedulerException {
		final JobDetail jobDetail = newJob(EchoJob.class).withIdentity("EchoJob", "Echo").build();

		logger.debug("Register CronTrigger for EchoJob");
		final CronTrigger trigger = newTrigger().withIdentity("EchoJobTrigger", "cron")
				.withSchedule(cronSchedule(ECHO_JOB)).forJob("EchoJob", "Echo").build();

		// schedule new
		sched.scheduleJob(jobDetail, trigger);
	}

}
