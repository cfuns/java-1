package de.benjaminborbe.dhl.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.dhl.status.DhlBean;
import de.benjaminborbe.dhl.status.DhlDao;
import de.benjaminborbe.dhl.util.DhlStatus;
import de.benjaminborbe.dhl.util.DhlStatusFetcher;
import de.benjaminborbe.dhl.util.DhlStatusFetcherException;
import de.benjaminborbe.dhl.util.DhlStatusNotifier;
import de.benjaminborbe.dhl.util.DhlStatusStorage;
import de.benjaminborbe.mail.api.MailSendException;
import de.benjaminborbe.storage.api.StorageException;

@Singleton
public class DhlStatusCheckCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 */5 * * * ?";

	private final Logger logger;

	private final DhlStatusFetcher dhlStatusFetcher;

	private final DhlStatusStorage dhlStatusStorage;

	private final DhlStatusNotifier dhlStatusNotifier;

	private final DhlDao dhlDao;

	@Inject
	public DhlStatusCheckCronJob(
			final Logger logger,
			final DhlStatusFetcher dhlStatusFetcher,
			final DhlStatusStorage dhlStatusStorage,
			final DhlStatusNotifier dhlStatusNotifier,
			final DhlDao dhlDao) {
		this.logger = logger;
		this.dhlStatusFetcher = dhlStatusFetcher;
		this.dhlStatusStorage = dhlStatusStorage;
		this.dhlStatusNotifier = dhlStatusNotifier;
		this.dhlDao = dhlDao;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		logger.trace("execute DhlCheckCronJob");
		try {
			for (final DhlBean dhl : dhlDao.getAll()) {
				try {
					final DhlStatus newStatus = dhlStatusFetcher.fetchStatus(dhl);
					logger.trace("newStatus: " + newStatus);

					final DhlStatus oldStatus = dhlStatusStorage.get(dhl.getId());
					logger.trace("oldStatus: " + oldStatus);

					if (newStatus != null) {
						dhlStatusStorage.store(newStatus);
						if (oldStatus == null || !oldStatus.equals(newStatus)) {
							dhlStatusNotifier.mailUpdate(newStatus);
							logger.info("status mailed");
						}
						else {
							logger.info("status not change, skip mail");
						}
					}
					else {
						logger.debug("can't find new status");
					}
				}
				catch (final DhlStatusFetcherException e) {
					logger.error(e.getClass().getSimpleName(), e);
				}
				catch (final MailSendException e) {
					logger.error(e.getClass().getSimpleName(), e);
				}
			}
		}
		catch (final StorageException e) {
			logger.error(e.getClass().getSimpleName(), e);
		}
	}
}
