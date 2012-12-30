package de.benjaminborbe.dhl.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.dhl.api.Dhl;
import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.dhl.api.DhlService;
import de.benjaminborbe.dhl.api.DhlServiceException;
import de.benjaminborbe.dhl.dao.DhlBean;
import de.benjaminborbe.dhl.dao.DhlDao;
import de.benjaminborbe.dhl.util.DhlStatus;
import de.benjaminborbe.dhl.util.DhlStatusFetcher;
import de.benjaminborbe.dhl.util.DhlStatusFetcherException;
import de.benjaminborbe.dhl.util.DhlStatusNotifier;
import de.benjaminborbe.dhl.util.DhlUrlBuilder;
import de.benjaminborbe.mail.api.MailServiceException;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;

@Singleton
public class DhlServiceImpl implements DhlService {

	private final Logger logger;

	private final DhlStatusFetcher dhlStatusFetcher;

	private final DhlStatusNotifier dhlStatusNotifier;

	private final DhlUrlBuilder dhlUrlBuilder;

	private final DhlDao dhlDao;

	@Inject
	public DhlServiceImpl(
			final Logger logger,
			final DhlStatusFetcher dhlStatusFetcher,
			final DhlStatusNotifier dhlStatusNotifier,
			final DhlUrlBuilder dhlUrlBuilder,
			final DhlDao dhlDao) {
		this.logger = logger;
		this.dhlStatusFetcher = dhlStatusFetcher;
		this.dhlStatusNotifier = dhlStatusNotifier;
		this.dhlUrlBuilder = dhlUrlBuilder;
		this.dhlDao = dhlDao;
	}

	@Override
	public void mailStatus(final SessionIdentifier sessionIdentifier, final DhlIdentifier dhlIdentifier) throws DhlServiceException {
		try {
			logger.trace("mailStatus - dhlIdentifier: " + dhlIdentifier);
			final Dhl dhl = dhlDao.load(dhlIdentifier);
			final DhlStatus newStatus = dhlStatusFetcher.fetchStatus(dhl);
			dhlStatusNotifier.mailUpdate(newStatus);
		}
		catch (final DhlStatusFetcherException e) {
			throw new DhlServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final MailServiceException e) {
			throw new DhlServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final StorageException e) {
			throw new DhlServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public Collection<DhlIdentifier> getRegisteredDhlIdentifiers(final SessionIdentifier sessionIdentifier) throws DhlServiceException {
		logger.debug("getRegisteredDhlIdentifiers");
		try {
			final List<DhlIdentifier> result = new ArrayList<DhlIdentifier>();
			final IdentifierIterator<DhlIdentifier> i = dhlDao.getIdentifierIterator();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		}
		catch (final StorageException e) {
			throw new DhlServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final IdentifierIteratorException e) {
			throw new DhlServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public void removeTracking(final SessionIdentifier sessionIdentifier, final DhlIdentifier dhlIdentifier) throws DhlServiceException {
		try {
			logger.debug("removeTracking");
			dhlDao.delete(dhlIdentifier);
		}
		catch (final StorageException e) {
			throw new DhlServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public URL buildDhlUrl(final SessionIdentifier sessionIdentifier, final DhlIdentifier dhlIdentifier) throws DhlServiceException {
		try {
			final Dhl dhl = dhlDao.load(dhlIdentifier);
			return dhlUrlBuilder.buildUrl(dhl);
		}
		catch (final MalformedURLException e) {
			throw new DhlServiceException("MalformedURLException", e);
		}
		catch (final StorageException e) {
			throw new DhlServiceException("MalformedURLException", e);
		}
	}

	@Override
	public DhlIdentifier createDhlIdentifier(final SessionIdentifier sessionIdentifier, final long id) throws DhlServiceException {
		return new DhlIdentifier(String.valueOf(id));
	}

	@Override
	public DhlIdentifier addTracking(final SessionIdentifier sessionIdentifier, final long trackingNumber, final long zip) throws DhlServiceException {
		try {
			final DhlBean bean = dhlDao.create();
			final DhlIdentifier id = createDhlIdentifier(sessionIdentifier, trackingNumber);
			bean.setId(id);
			bean.setTrackingNumber(trackingNumber);
			bean.setZip(zip);
			dhlDao.save(bean);
			return id;
		}
		catch (final StorageException e) {
			throw new DhlServiceException("MalformedURLException", e);
		}
	}

}
