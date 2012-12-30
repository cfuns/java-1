package de.benjaminborbe.message.service;

import com.google.inject.Inject;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.message.util.MessageConsumerExchanger;

public class MessageConsumerCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "*/15 * * * * ?";

	private final MessageConsumerExchanger messageConsumerExchanger;

	@Inject
	public MessageConsumerCronJob(final MessageConsumerExchanger messageConsumerExchanger) {
		this.messageConsumerExchanger = messageConsumerExchanger;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		messageConsumerExchanger.exchange();
	}

}
