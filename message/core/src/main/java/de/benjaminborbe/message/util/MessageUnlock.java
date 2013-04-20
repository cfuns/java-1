package de.benjaminborbe.message.util;

import javax.inject.Inject;
import de.benjaminborbe.message.dao.MessageBean;
import de.benjaminborbe.message.dao.MessageBeanMapper;
import de.benjaminborbe.message.dao.MessageDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.storage.tools.StorageValueList;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;
import org.slf4j.Logger;

import java.util.Calendar;

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
							logger.info("unlock message - type: " + bean.getType() + " id: " + bean.getId());
							messageDao.save(bean, new StorageValueList(getEncoding()).add(MessageBeanMapper.LOCK_NAME).add(MessageBeanMapper.LOCK_TIME));
							logger.debug("expired => unlock");
						} else {
							logger.debug("not expired => skip");
						}
					} else {
						logger.debug("not locked => skip");
					}
				}
				logger.trace("unlock message finished");
			} catch (final StorageException | EntityIteratorException e) {
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
		logger.trace("message-unlock - started");
		if (runOnlyOnceATime.run(new UnlockRunnable())) {
			logger.trace("message-unlock - finished");
			return true;
		} else {
			logger.trace("message-unlock - skipped");
			return false;
		}
	}

	private String getEncoding() {
		return messageDao.getEncoding();
	}
}
