package de.benjaminborbe.notification.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface NotificationService {

	void notify(UserIdentifier userIdentifier, String subject, String message) throws NotificationServiceException, ValidationException;

	void notify(SessionIdentifier sessionIdentifier, UserIdentifier userIdentifier, String subject, String message) throws NotificationServiceException, ValidationException,
			PermissionDeniedException, LoginRequiredException;

}
