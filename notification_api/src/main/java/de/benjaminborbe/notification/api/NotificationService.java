package de.benjaminborbe.notification.api;

import java.util.Collection;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface NotificationService {

	void notify(Notification notification) throws NotificationServiceException, ValidationException;

	void notify(final SessionIdentifier sessionIdentifier, Notification notification) throws NotificationServiceException, ValidationException, PermissionDeniedException,
			LoginRequiredException;

	Collection<NotificationTypeIdentifier> getNotificationTypeIdentifiers() throws NotificationServiceException;

	Collection<NotificationMediaIdentifier> getNotificationMediaIdentifiers() throws NotificationServiceException;

	void remove(SessionIdentifier sessionIdentifier, NotificationMediaIdentifier notificationMediaIdentifier, NotificationTypeIdentifier notificationTypeIdentifier)
			throws NotificationServiceException, PermissionDeniedException, LoginRequiredException;

	void add(SessionIdentifier sessionIdentifier, NotificationMediaIdentifier notificationMediaIdentifier, NotificationTypeIdentifier notificationTypeIdentifier)
			throws NotificationServiceException, PermissionDeniedException, LoginRequiredException;

	NotificationTypeIdentifier createNotificationTypeIdentifier(String id);

	NotificationMediaIdentifier createNotificationMediaIdentifier(String id);

	boolean isActive(SessionIdentifier sessionIdentifier, NotificationMediaIdentifier notificationMediaIdentifier, NotificationTypeIdentifier notificationTypeIdentifier)
			throws NotificationServiceException, PermissionDeniedException, LoginRequiredException;

}
