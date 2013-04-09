package de.benjaminborbe.message.api;

import java.util.Calendar;
import java.util.Collection;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface MessageService {

	MessageIdentifier createMessageIdentifier(String id) throws MessageServiceException;

	void deleteById(SessionIdentifier sessionIdentifier, MessageIdentifier messageIdentifier) throws MessageServiceException, PermissionDeniedException, LoginRequiredException;

	void deleteByType(SessionIdentifier sessionIdentifier, String type) throws MessageServiceException, PermissionDeniedException, LoginRequiredException;

	boolean exchangeMessages(final SessionIdentifier sessionIdentifier) throws MessageServiceException, LoginRequiredException, PermissionDeniedException;

	Collection<Message> getMessages(final SessionIdentifier sessionIdentifier) throws MessageServiceException, PermissionDeniedException, LoginRequiredException;

	void sendMessage(String type, String content) throws MessageServiceException;

	void sendMessage(String type, String id, String content) throws MessageServiceException;

	void sendMessage(String type, String id, String content, Calendar startTime) throws MessageServiceException;

	boolean unlockExpiredMessages(final SessionIdentifier sessionIdentifier) throws MessageServiceException, LoginRequiredException, PermissionDeniedException;

	String getLockName(SessionIdentifier sessionIdentifier) throws MessageServiceException, LoginRequiredException, PermissionDeniedException;

}
