package de.benjaminborbe.message.util;

import java.util.Calendar;

import com.google.inject.Inject;

import de.benjaminborbe.message.MessageConstants;
import de.benjaminborbe.message.dao.MessageBean;
import de.benjaminborbe.tools.date.CurrentTime;

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
		}
		else {
			return false;
		}
	}

}
