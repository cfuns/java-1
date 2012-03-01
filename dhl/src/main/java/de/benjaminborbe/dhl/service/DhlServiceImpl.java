package de.benjaminborbe.dhl.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.dhl.api.DhlService;
import de.benjaminborbe.dhl.api.DhlServiceException;
import de.benjaminborbe.dhl.util.DhlStatus;
import de.benjaminborbe.dhl.util.DhlStatusFetcher;
import de.benjaminborbe.dhl.util.DhlStatusFetcherException;
import de.benjaminborbe.dhl.util.DhlStatusNotifier;
import de.benjaminborbe.mail.api.MailSendException;

@Singleton
public class DhlServiceImpl implements DhlService {

	private final Logger logger;

	private final DhlStatusFetcher dhlStatusFetcher;

	private final DhlStatusNotifier dhlStatusNotifier;

	@Inject
	public DhlServiceImpl(final Logger logger, final DhlStatusFetcher dhlStatusFetcher, final DhlStatusNotifier dhlStatusNotifier) {
		this.logger = logger;
		this.dhlStatusFetcher = dhlStatusFetcher;
		this.dhlStatusNotifier = dhlStatusNotifier;
	}

	@Override
	public void mailStatus(final DhlIdentifier dhlIdentifier) throws DhlServiceException {
		try {
			logger.trace("mailStatus - dhlIdentifier: " + dhlIdentifier);
			final DhlStatus newStatus = dhlStatusFetcher.fetchStatus(dhlIdentifier);
			dhlStatusNotifier.mailUpdate(newStatus);
		}
		catch (final DhlStatusFetcherException e) {
			throw new DhlServiceException("DhlStatusFetcherException", e);
		}
		catch (final MailSendException e) {
			throw new DhlServiceException("MailSendException", e);
		}
	}

}
