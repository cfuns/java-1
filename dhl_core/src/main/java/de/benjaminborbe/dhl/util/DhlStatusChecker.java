package de.benjaminborbe.dhl.util;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.dhl.dao.DhlBean;
import de.benjaminborbe.dhl.dao.DhlDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.storage.tools.StorageValueList;

public class DhlStatusChecker {

	private final Logger logger;

	private final DhlStatusFetcher dhlStatusFetcher;

	private final DhlStatusNotifier dhlStatusNotifier;

	private final DhlDao dhlDao;

	@Inject
	public DhlStatusChecker(final Logger logger, final DhlStatusFetcher dhlStatusFetcher, final DhlStatusNotifier dhlStatusNotifier, final DhlDao dhlDao) {
		this.logger = logger;
		this.dhlStatusFetcher = dhlStatusFetcher;
		this.dhlStatusNotifier = dhlStatusNotifier;
		this.dhlDao = dhlDao;
	}

	public void check() {
		try {
			final EntityIterator<DhlBean> i = dhlDao.getEntityIterator();

			while (i.hasNext()) {
				final DhlBean dhl = i.next();
				try {
					final String oldStatusMessage = dhl.getStatus();
					logger.debug("oldStatus: " + oldStatusMessage);

					if (!"Die Sendung wurde erfolgreich zugestellt.".equals(oldStatusMessage)) {

						final DhlStatus newStatus = dhlStatusFetcher.fetchStatus(dhl);

						if (newStatus != null) {
							logger.debug("newStatus: " + newStatus.getMessage());

							if (oldStatusMessage == null || !oldStatusMessage.equals(newStatus.getMessage())) {

								dhl.setStatus(newStatus.getMessage());
								dhlDao.save(dhl, new StorageValueList(dhlDao.getEncoding()).add("status"));

								logger.debug("status changed from: " + oldStatusMessage + " to: " + newStatus.getMessage() + " send email");
								dhlStatusNotifier.notify(dhl.getOwner(), newStatus);
							}
							else {
								logger.debug("status not change, skip mail");
							}
						}
						else {
							logger.debug("can't find new status");
						}
					}
				}
				catch (final DhlStatusFetcherException | DhlStatusNotifierException e) {
					logger.error(e.getClass().getSimpleName(), e);
				}
			}
		}
		catch (final StorageException e) {
			logger.error(e.getClass().getSimpleName(), e);
		}
		catch (final EntityIteratorException e) {
			logger.error(e.getClass().getSimpleName(), e);
		}
	}
}
