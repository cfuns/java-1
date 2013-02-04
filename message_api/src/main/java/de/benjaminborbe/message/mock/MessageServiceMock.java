package de.benjaminborbe.message.mock;

import java.util.Collection;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.message.api.Message;
import de.benjaminborbe.message.api.MessageIdentifier;
import de.benjaminborbe.message.api.MessageService;
import de.benjaminborbe.message.api.MessageServiceException;

@Singleton
public class MessageServiceMock implements MessageService {

	@Inject
	public MessageServiceMock() {
	}

	@Override
	public void sendMessage(final String type, final String content) throws MessageServiceException {
	}

	@Override
	public boolean unlockExpiredMessages(final SessionIdentifier sessionIdentifier) {
		return false;
	}

	@Override
	public void sendMessage(final String type, final String id, final String content) throws MessageServiceException {
	}

	@Override
	public void deleteByType(final SessionIdentifier sessionIdentifier, final String type) throws MessageServiceException {
	}

	@Override
	public Collection<Message> getMessages(final SessionIdentifier sessionIdentifier) throws MessageServiceException, PermissionDeniedException, LoginRequiredException {
		return null;
	}

	@Override
	public void deleteById(final SessionIdentifier sessionIdentifier, final MessageIdentifier messageIdentifier) throws MessageServiceException, PermissionDeniedException,
			LoginRequiredException {
	}

	@Override
	public MessageIdentifier createMessageIdentifier(final String id) throws MessageServiceException {
		return new MessageIdentifier(id);
	}

}
