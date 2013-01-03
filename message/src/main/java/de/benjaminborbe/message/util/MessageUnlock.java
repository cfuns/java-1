package de.benjaminborbe.message.util;

import java.util.Calendar;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.message.dao.MessageBean;
import de.benjaminborbe.message.dao.MessageDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.storage.tools.StorageValueList;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;

public class MessageUnlock {

	private final class UnlockRunnable implements Runnable {

		@Override
		public void run() {

			try {
				logger.trace("unlock message started");
				final EntityIterator<MessageBean> i = messageDao.findExpired();
				while (i.hasNext()) {
					final MessageBean bean = i.next();

					final String lockName = bean.getLockName();
					final Calendar lockTime = bean.getLockTime();
					if (lockName != null && lockTime != null) {
						if (messageUtil.isMessageLockExpired(bean)) {
							bean.setLockName(null);
							bean.setLockTime(null);
							messageDao.save(bean, new StorageValueList(getEncoding()).add("lockName").add("lockTime"));
							logger.debug("expired => unlock");
						}
						else {
							logger.debug("not expired => skip");
						}
					}
					else {
						logger.debug("not locked => skip");
					}
				}
				logger.trace("unlock message finished");
			}
			catch (final StorageException e) {
				logger.warn(e.getClass().getName(), e);
			}
			catch (final EntityIteratorException e) {
				logger.warn(e.getClass().getName(), e);
			}
		}

	}

	private final Logger logger;

	private final MessageDao messageDao;

	private final RunOnlyOnceATime runOnlyOnceATime;

	private final MessageUtil messageUtil;

	@Inject
	public MessageUnlock(final Logger logger, final RunOnlyOnceATime runOnlyOnceATime, final MessageUtil messageUtil, final MessageDao messageDao) {
		this.logger = logger;
		this.runOnlyOnceATime = runOnlyOnceATime;
		this.messageUtil = messageUtil;
		this.messageDao = messageDao;
	}

	public boolean execute() {
		if (runOnlyOnceATime.run(new UnlockRunnable())) {
			logger.trace("exchange - run");
			return true;
		}
		else {
			logger.debug("exchange - skipped");
			return false;
		}
	}

	private String getEncoding() {
		return messageDao.getEncoding();
	}
}
