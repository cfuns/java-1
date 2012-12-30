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
import de.benjaminborbe.cron.message.CronMessage;
import de.benjaminborbe.cron.message.CronMessageMapper;
import de.benjaminborbe.message.api.MessageService;
import de.benjaminborbe.tools.date.DateUtil;

@Singleton
@DisallowConcurrentExecution
public class CronJobOsgi implements Job {

	private final Logger logger;

	private final CronMessageMapper cronMessageMapper;

	private final MessageService messageService;

	private final DateUtil dateUtil;

	@Inject
	public CronJobOsgi(final Logger logger, final MessageService messageService, final CronMessageMapper cronMessageMapper, final DateUtil dateUtil) {
		this.logger = logger;
		this.messageService = messageService;
		this.cronMessageMapper = cronMessageMapper;
		this.dateUtil = dateUtil;
	}

	@Override
	public void execute(final JobExecutionContext context) throws JobExecutionException {

		final Date fireTime = context.getFireTime();
		final String name = (String) context.getJobDetail().getJobDataMap().get(CronConstants.JOB_NAME);
		try {
			logger.debug("execute " + name + " at " + dateUtil.dateTimeString(fireTime));
			final CronMessage cronMessage = new CronMessage(name);
			final String id = dateUtil.dateTimeString(fireTime);
			final String content = cronMessageMapper.map(cronMessage);
			messageService.sendMessage(CronConstants.MESSSAGE_TYPE, id, content);
		}
		catch (final Exception e) {
			logger.info("execute - failed job: " + name + " exception: " + e.getClass().getSimpleName(), e);
		}
	}
}
