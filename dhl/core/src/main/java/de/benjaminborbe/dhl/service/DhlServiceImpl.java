package de.benjaminborbe.dhl.service;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.dhl.api.Dhl;
import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.dhl.api.DhlService;
import de.benjaminborbe.dhl.api.DhlServiceException;
import de.benjaminborbe.dhl.dao.DhlBean;
import de.benjaminborbe.dhl.dao.DhlDao;
import de.benjaminborbe.dhl.util.DhlStatus;
import de.benjaminborbe.dhl.util.DhlStatusChecker;
import de.benjaminborbe.dhl.util.DhlStatusFetcher;
import de.benjaminborbe.dhl.util.DhlStatusFetcherException;
import de.benjaminborbe.dhl.util.DhlStatusNotifier;
import de.benjaminborbe.dhl.util.DhlStatusNotifierException;
import de.benjaminborbe.dhl.util.DhlUrlBuilder;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.validation.ValidationExecutor;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
public class DhlServiceImpl implements DhlService {

	private final Logger logger;

	private final DhlStatusFetcher dhlStatusFetcher;

	private final DhlStatusNotifier dhlStatusNotifier;

	private final DhlUrlBuilder dhlUrlBuilder;

	private final DhlDao dhlDao;

	private final AuthenticationService authenticationService;

	private final AuthorizationService authorizationService;

	private final ValidationExecutor validationExecutor;

	private final DhlStatusChecker dhlStatusChecker;

	@Inject
	public DhlServiceImpl(
		final Logger logger,
		final DhlStatusChecker dhlStatusChecker,
		final ValidationExecutor validationExecutor,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final DhlStatusFetcher dhlStatusFetcher,
		final DhlStatusNotifier dhlStatusNotifier,
		final DhlUrlBuilder dhlUrlBuilder,
		final DhlDao dhlDao
	) {
		this.logger = logger;
		this.dhlStatusChecker = dhlStatusChecker;
		this.validationExecutor = validationExecutor;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
		this.dhlStatusFetcher = dhlStatusFetcher;
		this.dhlStatusNotifier = dhlStatusNotifier;
		this.dhlUrlBuilder = dhlUrlBuilder;
		this.dhlDao = dhlDao;
	}

	@Override
	public void triggerCheck(final SessionIdentifier sessionIdentifier) throws DhlServiceException, LoginRequiredException, PermissionDeniedException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			dhlStatusChecker.check();
		} catch (final AuthorizationServiceException e) {
			throw new DhlServiceException(e);
		}
	}

	@Override
	public void notifyStatus(final SessionIdentifier sessionIdentifier, final DhlIdentifier dhlIdentifier) throws DhlServiceException,
		PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(PERMISSION));
			logger.trace("notifyStatus - dhlIdentifier: " + dhlIdentifier);
			final DhlBean dhl = dhlDao.load(dhlIdentifier);
			final DhlStatus newStatus = dhlStatusFetcher.fetchStatus(dhl);
			dhlStatusNotifier.notify(dhl.getOwner(), newStatus);
		} catch (final DhlStatusFetcherException | StorageException | AuthorizationServiceException | DhlStatusNotifierException e) {
			throw new DhlServiceException(e);
		}
	}

	public Collection<DhlIdentifier> getIdentifiers(final SessionIdentifier sessionIdentifier) throws DhlServiceException, PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(PERMISSION));

			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);

			logger.debug("getRegisteredDhlIdentifiers");
			final List<DhlIdentifier> result = new ArrayList<>();
			final EntityIterator<DhlBean> i = dhlDao.getEntityIterator();
			while (i.hasNext()) {
				final DhlBean bean = i.next();
				if (userIdentifier.equals(bean.getOwner())) {
					result.add(bean.getId());
				}
			}
			return result;
		} catch (final StorageException | AuthorizationServiceException | EntityIteratorException | AuthenticationServiceException e) {
			throw new DhlServiceException(e);
		}
	}

	@Override
	public void removeTracking(final SessionIdentifier sessionIdentifier, final DhlIdentifier dhlIdentifier) throws DhlServiceException,
		PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(PERMISSION));

			logger.debug("removeTracking");
			dhlDao.delete(dhlIdentifier);
		} catch (final StorageException | AuthorizationServiceException e) {
			throw new DhlServiceException(e);
		}
	}

	@Override
	public URL buildDhlUrl(final SessionIdentifier sessionIdentifier, final DhlIdentifier dhlIdentifier) throws DhlServiceException,
		PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(PERMISSION));

			final Dhl dhl = dhlDao.load(dhlIdentifier);
			return dhlUrlBuilder.buildUrl(dhl);
		} catch (final ParseException | StorageException | AuthorizationServiceException e) {
			throw new DhlServiceException(e);
		}
	}

	@Override
	public DhlIdentifier createDhlIdentifier(final String id) {
		return id != null ? new DhlIdentifier(id) : null;
	}

	@Override
	public DhlIdentifier addTracking(final SessionIdentifier sessionIdentifier, final String trackingNumber, final long zip) throws DhlServiceException,
		PermissionDeniedException, ValidationException {
		try {
			authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(PERMISSION));

			final DhlBean bean = dhlDao.create();
			final DhlIdentifier id = createDhlIdentifier(trackingNumber);
			bean.setId(id);
			bean.setTrackingNumber(trackingNumber);
			bean.setZip(zip);
			bean.setOwner(authenticationService.getCurrentUser(sessionIdentifier));

			final ValidationResult errors = validationExecutor.validate(bean);
			if (errors.hasErrors()) {
				logger.warn(bean.getClass().getSimpleName() + " " + errors.toString());
				throw new ValidationException(errors);
			}

			dhlDao.save(bean);
			return id;
		} catch (final StorageException | AuthorizationServiceException | AuthenticationServiceException e) {
			throw new DhlServiceException(e);
		}
	}

	@Override
	public Collection<Dhl> getEntries(final SessionIdentifier sessionIdentifier) throws DhlServiceException, PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(PERMISSION));

			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);

			logger.debug("getRegisteredDhlIdentifiers");
			final List<Dhl> result = new ArrayList<>();
			final EntityIterator<DhlBean> i = dhlDao.getEntityIterator();
			while (i.hasNext()) {
				final DhlBean bean = i.next();
				if (userIdentifier.equals(bean.getOwner())) {
					result.add(bean);
				}
			}
			return result;
		} catch (final StorageException | AuthorizationServiceException | EntityIteratorException | AuthenticationServiceException e) {
			throw new DhlServiceException(e);
		}
	}

}
