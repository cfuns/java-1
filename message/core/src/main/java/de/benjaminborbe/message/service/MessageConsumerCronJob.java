package de.benjaminborbe.message.service;

import com.google.inject.Provider;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.message.config.MessageConfig;
import de.benjaminborbe.message.util.MessageConsumerExchanger;
import org.slf4j.Logger;

import javax.inject.Inject;

public class MessageConsumerCronJob implements CronJob {

	private final Provider<MessageConsumerExchanger> messageConsumerExchangerProvider;

	private final MessageConfig messageConfig;

	private final Logger logger;

	@Inject
	public MessageConsumerCronJob(
		final Logger logger,
		final Provider<MessageConsumerExchanger> messageConsumerExchangerProvider,
		final MessageConfig messageConfig
	) {
		this.logger = logger;
		this.messageConsumerExchangerProvider = messageConsumerExchangerProvider;
		this.messageConfig = messageConfig;
	}

	@Override
	public String getScheduleExpression() {
		return messageConfig.getScheduleExpression();
	}

	@Override
	public void execute() {
		logger.trace("message consumer - started");
		messageConsumerExchangerProvider.get().exchange();
		logger.trace("message consumer - finished");
	}

	@Override
	public boolean disallowConcurrentExecution() {
		return false;
	}

}
