package de.benjaminborbe.cron.service;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.cron.CronConstants;
import de.benjaminborbe.cron.message.CronMessage;
import de.benjaminborbe.cron.message.CronMessageMapper;
import de.benjaminborbe.cron.util.CronExecutor;
import de.benjaminborbe.message.api.Message;
import de.benjaminborbe.message.api.MessageConsumer;
import de.benjaminborbe.tools.mapper.MapException;

public class CronMessageConsumer implements MessageConsumer {

	private final Logger logger;

	private final CronMessageMapper cronMessageMapper;

	private final CronExecutor cronExecutor;

	@Inject
	public CronMessageConsumer(final Logger logger, final CronMessageMapper cronMessageMapper, final CronExecutor cronExecutor) {
		this.logger = logger;
		this.cronMessageMapper = cronMessageMapper;
		this.cronExecutor = cronExecutor;
	}

	@Override
	public String getType() {
		return CronConstants.MESSSAGE_TYPE;
	}

	@Override
	public boolean process(final Message message) {
		try {
			logger.debug("process");
			final CronMessage cronMessage = cronMessageMapper.map(message.getContent());
			cronExecutor.execute(cronMessage.getName());
			return true;
		}
		catch (final MapException e) {
			logger.warn(e.getClass().getName(), e);
			return false;
		}
	}

}
