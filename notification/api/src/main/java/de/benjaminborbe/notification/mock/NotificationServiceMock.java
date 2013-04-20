package de.benjaminborbe.notification.mock;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.notification.api.Notification;
import de.benjaminborbe.notification.api.NotificationMediaIdentifier;
import de.benjaminborbe.notification.api.NotificationService;
import de.benjaminborbe.notification.api.NotificationServiceException;
import de.benjaminborbe.notification.api.NotificationTypeIdentifier;

@Singleton
public class NotificationServiceMock implements NotificationService {

	@Inject
	public NotificationServiceMock() {
	}

	@Override
	public void remove(final SessionIdentifier sessionIdentifier, final NotificationMediaIdentifier notificationMediaIdentifier,
			final NotificationTypeIdentifier notificationTypeIdentifier) throws NotificationServiceException, PermissionDeniedException, LoginRequiredException {
	}

	@Override
	public void add(final SessionIdentifier sessionIdentifier, final NotificationMediaIdentifier notificationMediaIdentifier,
			final NotificationTypeIdentifier notificationTypeIdentifier) throws NotificationServiceException, PermissionDeniedException, LoginRequiredException {
	}

	@Override
	public NotificationTypeIdentifier createNotificationTypeIdentifier(final String id) {
		return null;
	}

	@Override
	public NotificationMediaIdentifier createNotificationMediaIdentifier(final String id) {
		return null;
	}

	@Override
	public Collection<NotificationTypeIdentifier> getNotificationTypeIdentifiers() throws NotificationServiceException {
		return null;
	}

	@Override
	public Collection<NotificationMediaIdentifier> getNotificationMediaIdentifiers() throws NotificationServiceException {
		return null;
	}

	@Override
	public boolean isActive(final SessionIdentifier sessionIdentifier, final NotificationMediaIdentifier notificationMediaIdentifier,
			final NotificationTypeIdentifier notificationTypeIdentifier) throws NotificationServiceException, PermissionDeniedException, LoginRequiredException {
		return false;
	}

	@Override
	public void notify(final Notification notification) throws NotificationServiceException, ValidationException {
	}

	@Override
	public void notify(final SessionIdentifier sessionIdentifier, final Notification notification) throws NotificationServiceException, ValidationException,
			PermissionDeniedException, LoginRequiredException {
	}

}
