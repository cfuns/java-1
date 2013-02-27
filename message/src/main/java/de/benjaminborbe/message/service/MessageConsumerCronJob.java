package de.benjaminborbe.message.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.message.util.MessageConsumerExchanger;

public class MessageConsumerCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "*/15 * * * * ?";

	private final Provider<MessageConsumerExchanger> messageConsumerExchangerProvider;

	private final Logger logger;

	@Inject
	public MessageConsumerCronJob(final Logger logger, final Provider<MessageConsumerExchanger> messageConsumerExchangerProvider) {
		this.logger = logger;
		this.messageConsumerExchangerProvider = messageConsumerExchangerProvider;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
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
