package de.benjaminborbe.message.service;

import com.google.inject.Inject;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.message.util.MessageUnlock;

public class MessageUnlockCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 * * * * ?";

	private final MessageUnlock messageUnlock;

	@Inject
	public MessageUnlockCronJob(final MessageUnlock messageUnlock) {
		this.messageUnlock = messageUnlock;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		messageUnlock.execute();
	}

	@Override
	public boolean disallowConcurrentExecution() {
		return true;
	}

}
