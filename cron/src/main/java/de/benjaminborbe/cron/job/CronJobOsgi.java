package de.benjaminborbe.cron.job;

import java.util.Date;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.CronConstants;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.cron.message.CronMessage;
import de.benjaminborbe.cron.message.CronMessageMapper;
import de.benjaminborbe.cron.util.CronExecutor;
import de.benjaminborbe.cron.util.CronJobRegistry;
import de.benjaminborbe.message.api.MessageService;
import de.benjaminborbe.tools.date.DateUtil;

@Singleton
@DisallowConcurrentExecution
public class CronJobOsgi implements Job {

	private final Logger logger;

	private final CronMessageMapper cronMessageMapper;

	private final MessageService messageService;

	private final DateUtil dateUtil;

	private final CronJobRegistry cronJobRegistry;

	private final CronExecutor cronExecutor;

	@Inject
	public CronJobOsgi(
			final Logger logger,
			final MessageService messageService,
			final CronMessageMapper cronMessageMapper,
			final DateUtil dateUtil,
			final CronJobRegistry cronJobRegistry,
			final CronExecutor cronExecutor) {
		this.logger = logger;
		this.messageService = messageService;
		this.cronMessageMapper = cronMessageMapper;
		this.dateUtil = dateUtil;
		this.cronJobRegistry = cronJobRegistry;
		this.cronExecutor = cronExecutor;
	}

	@Override
	public void execute(final JobExecutionContext context) throws JobExecutionException {
		final Date fireTime = context.getFireTime();
		final String name = (String) context.getJobDetail().getJobDataMap().get(CronConstants.JOB_NAME);
		try {
			logger.trace("execute " + name + " at " + dateUtil.dateTimeString(fireTime));
			final CronJob cronJob = cronJobRegistry.getByName(name);
			if (cronJob.disallowConcurrentExecution()) {
				final CronMessage cronMessage = new CronMessage(name);
				final String id = name; // + "_" + dateUtil.dateTimeString(fireTime);
				final String content = cronMessageMapper.map(cronMessage);
				logger.trace("send cron to queue");
				messageService.sendMessage(CronConstants.MESSAGE_TYPE, id, content);
			}
			else {
				logger.trace("execute cron directly");
				cronExecutor.execute(name);
			}
		}
		catch (final Exception e) {
			logger.info("execute - failed job: " + name + " exception: " + e.getClass().getSimpleName(), e);
		}
	}
}
