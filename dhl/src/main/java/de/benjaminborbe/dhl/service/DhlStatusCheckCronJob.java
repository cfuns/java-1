package de.benjaminborbe.dhl.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.dhl.util.DhlIdentifierRegistry;
import de.benjaminborbe.dhl.util.DhlStatus;
import de.benjaminborbe.dhl.util.DhlStatusFetcher;
import de.benjaminborbe.dhl.util.DhlStatusFetcherException;
import de.benjaminborbe.dhl.util.DhlStatusNotifier;
import de.benjaminborbe.dhl.util.DhlStatusStorage;
import de.benjaminborbe.mail.api.MailSendException;

@Singleton
public class DhlStatusCheckCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 */5 * * * ?";

	private final Logger logger;

	private final DhlIdentifierRegistry dhlIdentifierRegistry;

	private final DhlStatusFetcher dhlStatusFetcher;

	private final DhlStatusStorage dhlStatusStorage;

	private final DhlStatusNotifier dhlStatusNotifier;

	@Inject
	public DhlStatusCheckCronJob(
			final Logger logger,
			final DhlIdentifierRegistry dhlIdentifierRegistry,
			final DhlStatusFetcher dhlStatusFetcher,
			final DhlStatusStorage dhlStatusStorage,
			final DhlStatusNotifier dhlStatusNotifier) {
		this.logger = logger;
		this.dhlIdentifierRegistry = dhlIdentifierRegistry;
		this.dhlStatusFetcher = dhlStatusFetcher;
		this.dhlStatusStorage = dhlStatusStorage;
		this.dhlStatusNotifier = dhlStatusNotifier;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		logger.trace("execute DhlCheckCronJob");
		for (final DhlIdentifier dhlIdentifier : dhlIdentifierRegistry.getAll()) {
			try {
				final DhlStatus newStatus = dhlStatusFetcher.fetchStatus(dhlIdentifier);
				logger.trace("newStatus: " + newStatus);
				final DhlStatus oldStatus = dhlStatusStorage.get(dhlIdentifier);
				logger.trace("oldStatus: " + oldStatus);
				dhlStatusStorage.store(newStatus);
				if (oldStatus == null || !oldStatus.equals(newStatus)) {
					dhlStatusNotifier.mailUpdate(newStatus);
					logger.info("status mailed");
				}
				else {
					logger.info("status not change, skip mail");
				}
			}
			catch (final DhlStatusFetcherException e) {
				logger.error("DhlStatusFetcherException", e);
			}
			catch (final MailSendException e) {
				logger.error("MailSendException", e);
			}
		}
	}
}
