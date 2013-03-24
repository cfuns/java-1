package de.benjaminborbe.notification.service;

import java.util.Collection;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.notification.api.NotificationService;
import de.benjaminborbe.notification.api.NotificationServiceException;
import de.benjaminborbe.notification.util.NotifcationNotifier;
import de.benjaminborbe.notification.util.NotifcationNotifierDeterminer;
import de.benjaminborbe.notification.util.NotifcationNotifierException;

@Singleton
public class NotificationServiceImpl implements NotificationService {

	private final Logger logger;

	private final AuthorizationService authorizationService;

	private final NotifcationNotifierDeterminer notifcationNotifierDeterminer;

	@Inject
	public NotificationServiceImpl(final Logger logger, final AuthorizationService authorizationService, final NotifcationNotifierDeterminer notifcationNotifierDeterminer) {
		this.logger = logger;
		this.authorizationService = authorizationService;
		this.notifcationNotifierDeterminer = notifcationNotifierDeterminer;
	}

	@Override
	public void notify(final UserIdentifier userIdentifier, final String subject, final String message) throws NotificationServiceException {
		logger.debug("notifiy - user: " + userIdentifier + " message: " + message);
		final Collection<NotifcationNotifier> notifiers = notifcationNotifierDeterminer.getNotifcationNotifiers(userIdentifier);
		for (final NotifcationNotifier notifier : notifiers) {
			try {
				notifier.notify(userIdentifier, subject, message);
			}
			catch (final NotifcationNotifierException e) {
				logger.warn("send notification failed", e);
			}
		}
	}

	@Override
	public void notify(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier, final String subject, final String message)
			throws NotificationServiceException, ValidationException, PermissionDeniedException, LoginRequiredException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			notify(userIdentifier, subject, message);
		}
		catch (final AuthorizationServiceException e) {
			throw new NotificationServiceException(e);
		}
	}

}
