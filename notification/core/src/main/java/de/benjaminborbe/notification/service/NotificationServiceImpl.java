package de.benjaminborbe.notification.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

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
import de.benjaminborbe.notification.api.Notification;
import de.benjaminborbe.notification.api.NotificationMediaIdentifier;
import de.benjaminborbe.notification.api.NotificationService;
import de.benjaminborbe.notification.api.NotificationServiceException;
import de.benjaminborbe.notification.api.NotificationTypeIdentifier;
import de.benjaminborbe.notification.dao.NotificationMediaDao;
import de.benjaminborbe.notification.dao.NotificationTypeDao;
import de.benjaminborbe.notification.util.NotificationNotifier;
import de.benjaminborbe.notification.util.NotificationNotifierDeterminer;
import de.benjaminborbe.notification.util.NotificationNotifierException;
import de.benjaminborbe.notification.util.NotificationNotifierRegistry;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.tools.validation.ValidationResultImpl;

@Singleton
public class NotificationServiceImpl implements NotificationService {

	private final Logger logger;

	private final AuthorizationService authorizationService;

	private final NotificationNotifierDeterminer notifcationNotifierDeterminer;

	private final NotificationTypeDao notificationTypeDao;

	private final NotificationNotifierRegistry notifcationNotifierRegistry;

	private final AuthenticationService authenticationService;

	private final NotificationMediaDao notificationMediaDao;

	@Inject
	public NotificationServiceImpl(
			final Logger logger,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final NotificationNotifierRegistry notifcationNotifierRegistry,
			final NotificationNotifierDeterminer notifcationNotifierDeterminer,
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
	public void notify(final Notification notification) throws NotificationServiceException, ValidationException {
		try {

			logger.debug("notifiy - user: " + notification.getTo() + " message: " + notification.getMessage());

			if (notification.getTo() == null) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("username missing")));
			}
			if (notification.getType() == null) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("type missing")));
			}
			if (!(notification.getSubject() != null && !notification.getSubject().trim().isEmpty() || notification.getMessage() != null && !notification.getMessage().trim().isEmpty())) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("subject and message missing")));
			}

			notificationTypeDao.findOrCreate(notification.getType());

			final Collection<NotificationNotifier> notifiers = notifcationNotifierDeterminer.getNotifcationNotifiers(notification.getTo(), notification.getType());
			logger.debug("found " + notifiers.size() + " notifiers");
			for (final NotificationNotifier notifier : notifiers) {
				try {
					logger.debug("notify via " + notifier.getNotificationMediaIdentifier());
					notifier.notify(notification);
				}
				catch (final NotificationNotifierException e) {
					logger.warn("send notification  via " + notifier.getNotificationMediaIdentifier() + " failed", e);
				}
			}
		}
		catch (final StorageException | NotificationNotifierException e1) {
			throw new NotificationServiceException(e1);
		}
	}

	@Override
	public void notify(final SessionIdentifier sessionIdentifier, final Notification notification) throws NotificationServiceException, ValidationException,
			PermissionDeniedException, LoginRequiredException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			notify(notification);
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
