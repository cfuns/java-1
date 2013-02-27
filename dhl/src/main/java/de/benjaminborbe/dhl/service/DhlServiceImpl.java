package de.benjaminborbe.dhl.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
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
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;

@Singleton
public class DhlServiceImpl implements DhlService {

	private final Logger logger;

	private final DhlStatusFetcher dhlStatusFetcher;

	private final DhlStatusNotifier dhlStatusNotifier;

	private final DhlUrlBuilder dhlUrlBuilder;

	private final DhlDao dhlDao;

	private final AuthenticationService authenticationService;

	private final AuthorizationService authorizationService;

	@Inject
	public DhlServiceImpl(
			final Logger logger,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final DhlStatusFetcher dhlStatusFetcher,
			final DhlStatusNotifier dhlStatusNotifier,
			final DhlUrlBuilder dhlUrlBuilder,
			final DhlDao dhlDao) {
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
		this.dhlStatusFetcher = dhlStatusFetcher;
		this.dhlStatusNotifier = dhlStatusNotifier;
		this.dhlUrlBuilder = dhlUrlBuilder;
		this.dhlDao = dhlDao;
	}

	@Override
	public void mailStatus(final SessionIdentifier sessionIdentifier, final DhlIdentifier dhlIdentifier) throws DhlServiceException, LoginRequiredException,
			PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, new PermissionIdentifier(PERMISSION));

			logger.trace("mailStatus - dhlIdentifier: " + dhlIdentifier);
			final Dhl dhl = dhlDao.load(dhlIdentifier);
			final DhlStatus newStatus = dhlStatusFetcher.fetchStatus(dhl);
			dhlStatusNotifier.mailUpdate(newStatus);
		}
		catch (final DhlStatusFetcherException | MailServiceException | StorageException | AuthorizationServiceException e) {
			throw new DhlServiceException(e);
		}
	}

	@Override
	public Collection<DhlIdentifier> getRegisteredDhlIdentifiers(final SessionIdentifier sessionIdentifier) throws DhlServiceException, LoginRequiredException,
			PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, new PermissionIdentifier(PERMISSION));

			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);

			logger.debug("getRegisteredDhlIdentifiers");
			final List<DhlIdentifier> result = new ArrayList<DhlIdentifier>();
			final EntityIterator<DhlBean> i = dhlDao.getEntityIterator();
			while (i.hasNext()) {
				final DhlBean bean = i.next();
				if (userIdentifier.equals(bean.getOwner())) {
					result.add(bean.getId());
				}
			}
			return result;
		}
		catch (final StorageException | AuthorizationServiceException | EntityIteratorException | AuthenticationServiceException e) {
			throw new DhlServiceException(e);
		}
	}

	@Override
	public void removeTracking(final SessionIdentifier sessionIdentifier, final DhlIdentifier dhlIdentifier) throws DhlServiceException, LoginRequiredException,
			PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, new PermissionIdentifier(PERMISSION));

			logger.debug("removeTracking");
			dhlDao.delete(dhlIdentifier);
		}
		catch (final StorageException | AuthorizationServiceException e) {
			throw new DhlServiceException(e);
		}
	}

	@Override
	public URL buildDhlUrl(final SessionIdentifier sessionIdentifier, final DhlIdentifier dhlIdentifier) throws DhlServiceException, LoginRequiredException,
			PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, new PermissionIdentifier(PERMISSION));

			final Dhl dhl = dhlDao.load(dhlIdentifier);
			return dhlUrlBuilder.buildUrl(dhl);
		}
		catch (final MalformedURLException | StorageException | AuthorizationServiceException e) {
			throw new DhlServiceException(e);
		}
	}

	@Override
	public DhlIdentifier createDhlIdentifier(final String id) throws DhlServiceException {
		return id != null ? new DhlIdentifier(id) : null;
	}

	@Override
	public DhlIdentifier addTracking(final SessionIdentifier sessionIdentifier, final String trackingNumber, final long zip) throws DhlServiceException, LoginRequiredException,
			PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, new PermissionIdentifier(PERMISSION));

			final DhlBean bean = dhlDao.create();
			final DhlIdentifier id = createDhlIdentifier(trackingNumber);
			bean.setId(id);
			bean.setTrackingNumber(trackingNumber);
			bean.setZip(zip);
			dhlDao.save(bean);
			return id;
		}
		catch (final StorageException | AuthorizationServiceException e) {
			throw new DhlServiceException(e);
		}
	}

}
