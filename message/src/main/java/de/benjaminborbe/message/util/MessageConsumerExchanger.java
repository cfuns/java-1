package de.benjaminborbe.message.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsService;
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
				logger.trace("exchange - started");
				exchange(messageConsumer);
				logger.trace("exchange - finished");
			}
			catch (final Exception e) {
				logger.trace("exchange - failed", e);
			}
		}
	}

	private final class MessageConsumerExchangerRunnable implements Runnable {

		@Override
		public void run() {
			final List<Thread> threads = new ArrayList<Thread>();
			for (final MessageConsumer messageConsumer : messageConsumerRegistry.getAll()) {
				threads.add(threadRunner.run("messageConsumerExchange", new HandleConsumer(messageConsumer)));
			}
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

	private final AnalyticsService analyticsService;

	private final AnalyticsReportIdentifier analyticsReportIdentifierSuccess = new AnalyticsReportIdentifier("MessageSuccess");

	private final AnalyticsReportIdentifier analyticsReportIdentifierRetry = new AnalyticsReportIdentifier("MessageRetry");

	private final AnalyticsReportIdentifier analyticsReportIdentifierMaxRetry = new AnalyticsReportIdentifier("MessageMaxRetry");

	@Inject
	public MessageConsumerExchanger(
			final Logger logger,
			final AnalyticsService analyticsService,
			final RandomUtil randomUtil,
			final CalendarUtil calendarUtil,
			final MessageConsumerRegistry messageConsumerRegistry,
			final MessageDao messageDao,
			final RunOnlyOnceATime runOnlyOnceATime,
			final ThreadRunner threadRunner) {
		this.logger = logger;
		this.analyticsService = analyticsService;
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
		final long counter = message.getRetryCounter() != null ? message.getRetryCounter() : 0;
		final long maxRetry = message.getMaxRetryCounter() != null ? message.getMaxRetryCounter() : MessageConstants.MAX_RETRY;
		if (result) {
			messageDao.delete(message);
			track(analyticsReportIdentifierSuccess);
		}
		else if (counter >= maxRetry) {
			messageDao.delete(message);
			track(analyticsReportIdentifierMaxRetry);
		}
		else {
			final long increasedCounter = counter + 1;
			logger.debug("process message failed, increase retrycounter to " + increasedCounter);
			message.setRetryCounter(increasedCounter);
			messageDao.save(message);
			track(analyticsReportIdentifierRetry);
		}
		logger.trace("process message done");
	}

	private void track(final AnalyticsReportIdentifier id) {
		try {
			analyticsService.addReportValue(id);
		}
		catch (final Exception e) {
			logger.warn("track " + id + " failed", e);
		}
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
