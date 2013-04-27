package de.benjaminborbe.message.util;

import de.benjaminborbe.message.MessageConstants;
import de.benjaminborbe.message.dao.MessageBean;
import de.benjaminborbe.tools.date.CurrentTime;

import javax.inject.Inject;
import java.util.Calendar;

public class MessageUtil {

	private final CurrentTime currentTime;

	@Inject
	public MessageUtil(final CurrentTime currentTime) {
		this.currentTime = currentTime;
	}

	public boolean isMessageLockExpired(final MessageBean bean) {
		final String lockName = bean.getLockName();
		final Calendar lockTime = bean.getLockTime();
		if (lockName != null && lockTime != null) {
			return currentTime.currentTimeMillis() > lockTime.getTimeInMillis() + MessageConstants.CRON_EXPIRE;
		} else {
			return false;
		}
	}

}
