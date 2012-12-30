package de.benjaminborbe.message.util;

import java.util.Arrays;
import java.util.UUID;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.message.MessageConstants;
import de.benjaminborbe.message.api.MessageConsumer;
import de.benjaminborbe.message.dao.MessageBean;
import de.benjaminborbe.message.dao.MessageDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;
import de.benjaminborbe.tools.util.RandomUtil;

@Singleton
public class MessageConsumerExchanger {

	private final class MessageConsumerExchangerRunnable implements Runnable {

		@Override
		public void run() {
			logger.debug("exchange - started");
			for (final MessageConsumer messageConsumer : messageConsumerRegistry.getAll()) {
				try {
					exchange(messageConsumer);
				}
				catch (final StorageException e) {
					logger.warn(e.getClass().getName(), e);
				}
				catch (final EntityIteratorException e) {
					logger.warn(e.getClass().getName(), e);
				}
			}
			logger.debug("exchange - finished");
		}
	}

	private final MessageConsumerRegistry messageConsumerRegistry;

	private final MessageDao messageDao;

	private final Logger logger;

	private final RunOnlyOnceATime runOnlyOnceATime;

	private final String lockName;

	private final CalendarUtil calendarUtil;

	private final RandomUtil randomUtil;

	@Inject
	public MessageConsumerExchanger(
			final Logger logger,
			final RandomUtil randomUtil,
			final CalendarUtil calendarUtil,
			final MessageConsumerRegistry messageConsumerRegistry,
			final MessageDao messageDao,
			final RunOnlyOnceATime runOnlyOnceATime) {
		this.logger = logger;
		this.randomUtil = randomUtil;
		this.calendarUtil = calendarUtil;
		this.messageConsumerRegistry = messageConsumerRegistry;
		this.messageDao = messageDao;
		this.runOnlyOnceATime = runOnlyOnceATime;
		this.lockName = String.valueOf(UUID.randomUUID());
	}

	public void exchange() {
		if (runOnlyOnceATime.run(new MessageConsumerExchangerRunnable())) {
			logger.debug("exchange - run");
		}
		else {
			logger.debug("exchange - skipped");
		}
	}

	private void exchange(final MessageConsumer messageConsumer) throws StorageException, EntityIteratorException {
		final EntityIterator<MessageBean> i = messageDao.getEntityIteratorForUser(messageConsumer.getType());
		while (i.hasNext()) {
			final MessageBean message = i.next();
			exchange(messageConsumer, message);
		}
	}

	private void exchange(final MessageConsumer messageConsumer, final MessageBean message) throws StorageException {
		logger.debug("process message - type: " + message.getType() + " retryCounter: " + message.getRetryCounter());
		boolean result = false;
		try {
			if (!lock(message)) {
				logger.debug("lock message failed => skip");
				return;
			}

			result = messageConsumer.process(message);
		}
		catch (final Exception e) {
			logger.warn("process message failed", e);
			result = false;
		}
		long counter = message.getRetryCounter() != null ? message.getRetryCounter() : 0;
		if (result || counter >= MessageConstants.MAX_RETRY) {
			messageDao.delete(message);
		}
		else {
			counter++;
			logger.debug("process message failed, increase retrycounter to " + counter);
			message.setRetryCounter(counter);
			messageDao.save(message);
		}
		logger.debug("process message done");
	}

	private boolean lock(final MessageBean message) throws StorageException {
		if (message.getLockTime() == null) {
			message.setLockName(lockName);
			message.setLockTime(calendarUtil.now());
			messageDao.save(message, Arrays.asList("lockTime", "lockName"));

			try {
				Thread.sleep(randomUtil.getRandomized(1000, 100));
			}
			catch (final InterruptedException e) {
			}

			messageDao.load(message, Arrays.asList("lockName"));

			return lockName.equals(message.getLockName());
		}
		else if (lockName.equals(message.getLockName())) {
			message.setLockTime(calendarUtil.now());
			messageDao.save(message, Arrays.asList("lockTime"));
			return true;
		}
		else {
			return false;
		}
	}
}
