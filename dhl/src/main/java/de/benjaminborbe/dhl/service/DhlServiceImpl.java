package de.benjaminborbe.dhl.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.dhl.api.DhlService;
import de.benjaminborbe.dhl.api.DhlServiceException;
import de.benjaminborbe.dhl.util.DhlIdentifierRegistry;
import de.benjaminborbe.dhl.util.DhlStatus;
import de.benjaminborbe.dhl.util.DhlStatusFetcher;
import de.benjaminborbe.dhl.util.DhlStatusFetcherException;
import de.benjaminborbe.dhl.util.DhlStatusNotifier;
import de.benjaminborbe.dhl.util.DhlUrlBuilder;
import de.benjaminborbe.mail.api.MailSendException;

@Singleton
public class DhlServiceImpl implements DhlService {

	private final Logger logger;

	private final DhlStatusFetcher dhlStatusFetcher;

	private final DhlStatusNotifier dhlStatusNotifier;

	private final DhlIdentifierRegistry dhlIdentifierRegistry;

	private final DhlUrlBuilder dhlUrlBuilder;

	@Inject
	public DhlServiceImpl(
			final Logger logger,
			final DhlStatusFetcher dhlStatusFetcher,
			final DhlStatusNotifier dhlStatusNotifier,
			final DhlIdentifierRegistry dhlIdentifierRegistry,
			final DhlUrlBuilder dhlUrlBuilder) {
		this.logger = logger;
		this.dhlStatusFetcher = dhlStatusFetcher;
		this.dhlStatusNotifier = dhlStatusNotifier;
		this.dhlIdentifierRegistry = dhlIdentifierRegistry;
		this.dhlUrlBuilder = dhlUrlBuilder;
	}

	@Override
	public void mailStatus(final SessionIdentifier sessionIdentifier, final DhlIdentifier dhlIdentifier) throws DhlServiceException {
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

	@Override
	public Collection<DhlIdentifier> getRegisteredDhlIdentifiers(final SessionIdentifier sessionIdentifier) throws DhlServiceException {
		logger.debug("getRegisteredDhlIdentifiers");
		return dhlIdentifierRegistry.getAll();
	}

	@Override
	public DhlIdentifier createDhlIdentifier(final SessionIdentifier sessionIdentifier, final long id, final long zip) throws DhlServiceException {
		logger.debug("createDhlIdentifier");
		return new DhlIdentifier(id, zip);
	}

	@Override
	public boolean removeTracking(final SessionIdentifier sessionIdentifier, final DhlIdentifier dhlIdentifier) throws DhlServiceException {
		logger.debug("removeTracking");
		dhlIdentifierRegistry.remove(dhlIdentifier);
		return true;
	}

	@Override
	public boolean addTracking(final SessionIdentifier sessionIdentifier, final DhlIdentifier dhlIdentifier) throws DhlServiceException {
		logger.debug("addTracking");
		dhlIdentifierRegistry.add(dhlIdentifier);
		return true;
	}

	@Override
	public URL buildDhlUrl(final DhlIdentifier dhlIdentifier) throws DhlServiceException {
		try {
			return dhlUrlBuilder.buildUrl(dhlIdentifier);
		}
		catch (final MalformedURLException e) {
			throw new DhlServiceException("MalformedURLException", e);
		}
	}

}
