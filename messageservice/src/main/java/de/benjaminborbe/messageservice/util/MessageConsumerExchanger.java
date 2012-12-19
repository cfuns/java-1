package de.benjaminborbe.messageservice.util;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.messageservice.MessageserviceConstants;
import de.benjaminborbe.messageservice.api.MessageConsumer;
import de.benjaminborbe.messageservice.dao.MessageBean;
import de.benjaminborbe.messageservice.dao.MessageDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;

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

	@Inject
	public MessageConsumerExchanger(final Logger logger, final MessageConsumerRegistry messageConsumerRegistry, final MessageDao messageDao, final RunOnlyOnceATime runOnlyOnceATime) {
		this.logger = logger;
		this.messageConsumerRegistry = messageConsumerRegistry;
		this.messageDao = messageDao;
		this.runOnlyOnceATime = runOnlyOnceATime;
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
			logger.debug("process message - type: " + message.getType() + " retryCounter: " + message.getRetryCounter());
			boolean result = false;
			try {
				result = messageConsumer.process(message);
			}
			catch (final Exception e) {
				logger.warn("process message failed", e);
				result = false;
			}
			long counter = message.getRetryCounter() != null ? message.getRetryCounter() : 0;
			if (result || counter >= MessageserviceConstants.MAX_RETRY) {
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
	}
}
