package de.benjaminborbe.messageservice.service;

import com.google.inject.Inject;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.messageservice.util.MessageConsumerExchanger;

public class MessageConsumerCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "*/15 * * * * ?"; // ones per hour

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
