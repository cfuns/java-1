package de.benjaminborbe.message.util;

import java.util.ArrayList;
import java.util.Calendar;
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
import de.benjaminborbe.tools.date.TimeZoneUtil;
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
				logger.debug("exchange message started for " + messageConsumer.getType());
				exchange(messageConsumer);
				logger.debug("exchange message finshed for " + messageConsumer.getType());
			}
			catch (final Exception e) {
				logger.debug("exchange message failed for " + messageConsumer.getType(), e);
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

	private static final long DELAY_PER_RETRY = 1 * 60 * 1000;

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

	private final TimeZoneUtil timeZoneUtil;

	@Inject
	public MessageConsumerExchanger(
			final Logger logger,
			final AnalyticsService analyticsService,
			final RandomUtil randomUtil,
			final CalendarUtil calendarUtil,
			final MessageConsumerRegistry messageConsumerRegistry,
			final MessageDao messageDao,
			final RunOnlyOnceATime runOnlyOnceATime,
			final ThreadRunner threadRunner,
			final TimeZoneUtil timeZoneUtil) {
		this.logger = logger;
		this.analyticsService = analyticsService;
		this.randomUtil = randomUtil;
		this.calendarUtil = calendarUtil;
		this.messageConsumerRegistry = messageConsumerRegistry;
		this.messageDao = messageDao;
		this.runOnlyOnceATime = runOnlyOnceATime;
		this.threadRunner = threadRunner;
		this.timeZoneUtil = timeZoneUtil;
		this.lockName = String.valueOf(UUID.randomUUID());
	}

	public boolean exchange() {
		logger.debug("message consume - started");
		if (runOnlyOnceATime.run(new MessageConsumerExchangerRunnable())) {
			logger.debug("message consume - finished");
			return true;
		}
		else {
			logger.debug("message consume - skipped");
			return false;
		}
	}

	private void exchange(final MessageConsumer messageConsumer) throws StorageException, EntityIteratorException {
		final EntityIterator<MessageBean> i = messageDao.getEntityIteratorForType(messageConsumer.getType());
		while (i.hasNext()) {
			final MessageBean message = i.next();
			exchange(messageConsumer, message);
		}
	}

	private void exchange(final MessageConsumer messageConsumer, final MessageBean message) throws StorageException {
		{
			final Calendar startTime = message.getStartTime();
			if (startTime != null) {
				final Calendar now = calendarUtil.now();
				if (startTime.getTimeInMillis() > now.getTimeInMillis()) {
					logger.debug("startTime not reached " + startTime.getTimeInMillis() + " > " + now.getTimeInMillis() + " => skip");
					return;
				}
				else {
					logger.debug("startTime reached " + startTime.getTimeInMillis() + " <= " + now.getTimeInMillis());
				}
			}
			else {
				logger.debug("startTime not defined");
			}

		}

		boolean result = false;
		try {
			if (!lock(message)) {
				logger.debug("lock message failed => skip");
				return;
			}
			logger.debug("process message - type: " + message.getType() + " retryCounter: " + message.getRetryCounter());
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
			message.setStartTime(calcStartTime(increasedCounter));
			messageDao.save(message);
			track(analyticsReportIdentifierRetry);
		}
		logger.debug("process message done");
	}

	private Calendar calcStartTime(final long retryCounter) {
		final long time = calendarUtil.getTime() + retryCounter * DELAY_PER_RETRY;
		return calendarUtil.getCalendar(timeZoneUtil.getUTCTimeZone(), time);
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
			final Calendar now = calendarUtil.now();
			logger.debug("try lock message - lockName: " + lockName + " lockTime: " + calendarUtil.toDateTimeString(now));
			message.setLockName(lockName);
			message.setLockTime(now);
			messageDao.save(message, new StorageValueList(getEncoding()).add("lockTime").add("lockName"));

			try {
				Thread.sleep(randomUtil.getRandomized(10000, 50));
			}
			catch (final InterruptedException e) {
			}

			messageDao.load(message, new StorageValueList(getEncoding()).add("lockName"));

			if (lockName.equals(message.getLockName())) {
				logger.debug("lock message success - id: " + message.getId());
				return true;
			}
			else {
				logger.debug("lock message failed - id: " + message.getId());
				return false;
			}
		}
		else if (lockName.equals(message.getLockName())) {
			final Calendar now = calendarUtil.now();
			logger.debug("update message lock - id: " + message.getId() + " lockName: " + lockName + " lockTime: " + calendarUtil.toDateTimeString(now));
			message.setLockTime(now);
			messageDao.save(message, new StorageValueList(getEncoding()).add("lockTime"));
			return true;
		}
		else {
			logger.debug("lock message failed - id: " + message.getId());
			return false;
		}
	}

	private String getEncoding() {
		return messageDao.getEncoding();
	}
}
