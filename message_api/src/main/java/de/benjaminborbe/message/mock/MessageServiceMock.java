package de.benjaminborbe.message.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
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

}
