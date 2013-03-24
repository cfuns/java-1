package de.benjaminborbe.notification.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.notification.api.NotificationMediaIdentifier;
import de.benjaminborbe.notification.api.NotificationService;
import de.benjaminborbe.notification.api.NotificationServiceException;
import de.benjaminborbe.notification.api.NotificationTypeIdentifier;
import de.benjaminborbe.notification.dao.NotificationMediaDao;
import de.benjaminborbe.notification.dao.NotificationTypeDao;
import de.benjaminborbe.notification.util.NotifcationNotifier;
import de.benjaminborbe.notification.util.NotifcationNotifierDeterminer;
import de.benjaminborbe.notification.util.NotifcationNotifierException;
import de.benjaminborbe.notification.util.NotifcationNotifierRegistry;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.tools.validation.ValidationResultImpl;

@Singleton
public class NotificationServiceImpl implements NotificationService {

	private final Logger logger;

	private final AuthorizationService authorizationService;

	private final NotifcationNotifierDeterminer notifcationNotifierDeterminer;

	private final NotificationTypeDao notificationTypeDao;

	private final NotifcationNotifierRegistry notifcationNotifierRegistry;

	private final AuthenticationService authenticationService;

	private final NotificationMediaDao notificationMediaDao;

	@Inject
	public NotificationServiceImpl(
			final Logger logger,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final NotifcationNotifierRegistry notifcationNotifierRegistry,
			final NotifcationNotifierDeterminer notifcationNotifierDeterminer,
			final NotificationMediaDao notificationMediaDao,
			final NotificationTypeDao notificationTypeDao) {
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
		this.notifcationNotifierRegistry = notifcationNotifierRegistry;
		this.notifcationNotifierDeterminer = notifcationNotifierDeterminer;
		this.notificationMediaDao = notificationMediaDao;
		this.notificationTypeDao = notificationTypeDao;
	}

	@Override
	public void notify(final UserIdentifier userIdentifier, final NotificationTypeIdentifier type, final String subject, final String message) throws NotificationServiceException,
			ValidationException {
		try {
			logger.debug("notifiy - user: " + userIdentifier + " message: " + message);

			if (userIdentifier == null) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("username missing")));
			}
			if (type == null) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("type missing")));
			}
			if (!(subject != null && !subject.trim().isEmpty() || message != null && !message.trim().isEmpty())) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("subject and message missing")));
			}

			notificationTypeDao.findOrCreate(type);

			final Collection<NotifcationNotifier> notifiers = notifcationNotifierDeterminer.getNotifcationNotifiers(userIdentifier, type);
			logger.debug("found " + notifiers.size() + " notifiers");
			for (final NotifcationNotifier notifier : notifiers) {
				try {
					logger.debug("notify via " + notifier.getNotificationMediaIdentifier());
					notifier.notify(userIdentifier, subject, message);
				}
				catch (final NotifcationNotifierException e) {
					logger.warn("send notification  via " + notifier.getNotificationMediaIdentifier() + " failed", e);
				}
			}
		}
		catch (final StorageException | NotifcationNotifierException e1) {
			throw new NotificationServiceException(e1);
		}
	}

	@Override
	public void notify(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier, final NotificationTypeIdentifier type, final String subject,
			final String message) throws NotificationServiceException, ValidationException, PermissionDeniedException, LoginRequiredException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			notify(userIdentifier, type, subject, message);
		}
		catch (final AuthorizationServiceException e) {
			throw new NotificationServiceException(e);
		}
	}

	@Override
	public Collection<NotificationTypeIdentifier> getNotificationTypeIdentifiers() throws NotificationServiceException {
		try {
			final List<NotificationTypeIdentifier> result = new ArrayList<>();
			final IdentifierIterator<NotificationTypeIdentifier> i = notificationTypeDao.getIdentifierIterator();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		}
		catch (final StorageException | IdentifierIteratorException e) {
			throw new NotificationServiceException(e);
		}
	}

	@Override
	public Collection<NotificationMediaIdentifier> getNotificationMediaIdentifiers() throws NotificationServiceException {
		return notifcationNotifierRegistry.getMedias();
	}

	@Override
	public void add(final SessionIdentifier sessionIdentifier, final NotificationMediaIdentifier notificationMediaIdentifier,
			final NotificationTypeIdentifier notificationTypeIdentifier) throws NotificationServiceException, PermissionDeniedException, LoginRequiredException {
		try {
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			notificationMediaDao.add(userIdentifier, notificationTypeIdentifier, notificationMediaIdentifier);
		}
		catch (final StorageException | AuthenticationServiceException e) {
			throw new NotificationServiceException(e);
		}
	}

	@Override
	public void remove(final SessionIdentifier sessionIdentifier, final NotificationMediaIdentifier notificationMediaIdentifier,
			final NotificationTypeIdentifier notificationTypeIdentifier) throws NotificationServiceException, PermissionDeniedException, LoginRequiredException {
		try {
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			notificationMediaDao.remove(userIdentifier, notificationTypeIdentifier, notificationMediaIdentifier);
		}
		catch (final StorageException | AuthenticationServiceException e) {
			throw new NotificationServiceException(e);
		}
	}

	@Override
	public boolean isActive(final SessionIdentifier sessionIdentifier, final NotificationMediaIdentifier notificationMediaIdentifier,
			final NotificationTypeIdentifier notificationTypeIdentifier) throws NotificationServiceException, PermissionDeniedException, LoginRequiredException {
		try {
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			return notificationMediaDao.has(userIdentifier, notificationTypeIdentifier, notificationMediaIdentifier);
		}
		catch (final StorageException | AuthenticationServiceException e) {
			throw new NotificationServiceException(e);
		}
	}

	@Override
	public NotificationTypeIdentifier createNotificationTypeIdentifier(final String id) {
		if (id != null && !id.trim().isEmpty()) {
			return new NotificationTypeIdentifier(id);
		}
		else {
			return null;
		}
	}

	@Override
	public NotificationMediaIdentifier createNotificationMediaIdentifier(final String id) {
		if (id != null && !id.trim().isEmpty()) {
			return new NotificationMediaIdentifier(id);
		}
		else {
			return null;
		}
	}
}
