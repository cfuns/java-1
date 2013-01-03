package de.benjaminborbe.message.api;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface MessageService {

	void sendMessage(String type, String content) throws MessageServiceException;

	void sendMessage(String type, String id, String content) throws MessageServiceException;

	boolean unlockExpiredMessages(final SessionIdentifier sessionIdentifier) throws MessageServiceException, LoginRequiredException, PermissionDeniedException;

	void deleteByType(SessionIdentifier sessionIdentifier, String type) throws MessageServiceException, PermissionDeniedException, LoginRequiredException;

}
