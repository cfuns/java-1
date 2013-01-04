package de.benjaminborbe.message.util;

import java.util.ArrayList;
import java.util.List;
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
import de.benjaminborbe.storage.tools.StorageValueList;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;
import de.benjaminborbe.tools.util.RandomUtil;
import de.benjaminborbe.tools.util.ThreadRunner;

@Singleton
public class MessageConsumerExchanger {

	private final class HandleConsumer implements Runnable {

		private final MessageConsumer messageConsumer;

		private HandleConsumer(final MessageConsumer messageConsumer) {
			this.messageConsumer = messageConsumer;
		}

		@Override
		public void run() {
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
	}

	private final class MessageConsumerExchangerRunnable implements Runnable {

		@Override
		public void run() {
			logger.trace("exchange - started");

			final List<Thread> threads = new ArrayList<Thread>();

			for (final MessageConsumer messageConsumer : messageConsumerRegistry.getAll()) {
				threads.add(threadRunner.run("messageConsumerExchange", new HandleConsumer(messageConsumer)));
			}

			for (final Thread thread : threads) {
				try {
					thread.join();
				}
				catch (final InterruptedException e) {
				}
			}

			logger.trace("exchange - finished");
		}
	}

	private final MessageConsumerRegistry messageConsumerRegistry;

	private final MessageDao messageDao;

	private final Logger logger;

	private final RunOnlyOnceATime runOnlyOnceATime;

	private final String lockName;

	private final CalendarUtil calendarUtil;

	private final RandomUtil randomUtil;

	private final ThreadRunner threadRunner;

	@Inject
	public MessageConsumerExchanger(
			final Logger logger,
			final RandomUtil randomUtil,
			final CalendarUtil calendarUtil,
			final MessageConsumerRegistry messageConsumerRegistry,
			final MessageDao messageDao,
			final RunOnlyOnceATime runOnlyOnceATime,
			final ThreadRunner threadRunner) {
		this.logger = logger;
		this.randomUtil = randomUtil;
		this.calendarUtil = calendarUtil;
		this.messageConsumerRegistry = messageConsumerRegistry;
		this.messageDao = messageDao;
		this.runOnlyOnceATime = runOnlyOnceATime;
		this.threadRunner = threadRunner;
		this.lockName = String.valueOf(UUID.randomUUID());
	}

	public boolean exchange() {
		logger.trace("message consume - started");
		if (runOnlyOnceATime.run(new MessageConsumerExchangerRunnable())) {
			logger.trace("message consume - finished");
			return true;
		}
		else {
			logger.trace("message consume - skipped");
			return false;
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
		boolean result = false;
		try {
			if (!lock(message)) {
				logger.trace("lock message failed => skip");
				return;
			}
			logger.trace("process message - type: " + message.getType() + " retryCounter: " + message.getRetryCounter());
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
		logger.trace("process message done");
	}

	private boolean lock(final MessageBean message) throws StorageException {
		if (message.getLockTime() == null) {
			message.setLockName(lockName);
			message.setLockTime(calendarUtil.now());
			messageDao.save(message, new StorageValueList(getEncoding()).add("lockTime").add("lockName"));

			try {
				Thread.sleep(randomUtil.getRandomized(1000, 100));
			}
			catch (final InterruptedException e) {
			}

			messageDao.load(message, new StorageValueList(getEncoding()).add("lockName"));

			return lockName.equals(message.getLockName());
		}
		else if (lockName.equals(message.getLockName())) {
			message.setLockTime(calendarUtil.now());
			messageDao.save(message, new StorageValueList(getEncoding()).add("lockTime"));
			return true;
		}
		else {
			return false;
		}
	}

	private String getEncoding() {
		return messageDao.getEncoding();
	}
}
