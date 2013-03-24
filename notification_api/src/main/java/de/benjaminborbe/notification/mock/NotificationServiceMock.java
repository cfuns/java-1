package de.benjaminborbe.notification.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.notification.api.NotificationService;
import de.benjaminborbe.notification.api.NotificationServiceException;

@Singleton
public class NotificationServiceMock implements NotificationService {

	@Inject
	public NotificationServiceMock() {
	}

	@Override
	public void notify(final UserIdentifier userIdentifier, final String subject, final String message) throws NotificationServiceException, ValidationException {
	}

	@Override
	public void notify(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier, final String subject, final String message) throws NotificationServiceException, ValidationException,
			PermissionDeniedException, LoginRequiredException {
	}

}
