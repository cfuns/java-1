package de.benjaminborbe.cron.util;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.cron.CronConstants;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.cron.message.CronMessage;
import de.benjaminborbe.cron.message.CronMessageMapper;
import de.benjaminborbe.message.api.MessageService;
import de.benjaminborbe.message.api.MessageServiceException;
import de.benjaminborbe.tools.mapper.MapException;

public class CronMessageSender {

	private final CronMessageMapper cronMessageMapper;

	private final MessageService messageService;

	private final CronJobRegistry cronJobRegistry;

	private final CronExecutor cronExecutor;

	private final Logger logger;

	@Inject
	public CronMessageSender(
			final Logger logger,
			final CronExecutor cronExecutor,
			final CronJobRegistry cronJobRegistry,
			final MessageService messageService,
			final CronMessageMapper cronMessageMapper) {
		this.logger = logger;
		this.cronExecutor = cronExecutor;
		this.cronJobRegistry = cronJobRegistry;
		this.messageService = messageService;
		this.cronMessageMapper = cronMessageMapper;
	}

	public void send(final String name) throws MapException, MessageServiceException {
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

}
