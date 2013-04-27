package de.benjaminborbe.cron.util;

import de.benjaminborbe.cron.CronConstants;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.cron.message.CronMessage;
import de.benjaminborbe.cron.message.CronMessageMapper;
import de.benjaminborbe.message.api.MessageService;
import de.benjaminborbe.message.api.MessageServiceException;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.util.ThreadRunner;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CronMessageSender {

	private final class ParallelCronRunnable implements Runnable {

		private final String name;

		private ParallelCronRunnable(final String name) {
			this.name = name;
		}

		@Override
		public void run() {
			cronExecutor.execute(name);
		}
	}

	private final CronMessageMapper cronMessageMapper;

	private final MessageService messageService;

	private final CronJobRegistry cronJobRegistry;

	private final CronExecutor cronExecutor;

	private final Logger logger;

	private final ThreadRunner threadRunner;

	@Inject
	public CronMessageSender(
		final Logger logger,
		final ThreadRunner threadRunner,
		final CronExecutor cronExecutor,
		final CronJobRegistry cronJobRegistry,
		final MessageService messageService,
		final CronMessageMapper cronMessageMapper
	) {
		this.logger = logger;
		this.threadRunner = threadRunner;
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
			logger.trace("send cron to queue - name: " + name);
			messageService.sendMessage(CronConstants.MESSAGE_TYPE, id, content);
		} else {
			logger.trace("exec cron direct - name: " + name);
			threadRunner.run("exec direct", new ParallelCronRunnable(name));
		}
	}

}
